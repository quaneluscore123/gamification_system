package com.lifequest.app.domain.game

import com.lifequest.app.domain.inventory.InventoryRules
import com.lifequest.app.domain.player.PlayerRules
import com.lifequest.app.domain.quest.QuestCatalog
import com.lifequest.app.domain.quest.QuestProgress
import com.lifequest.app.domain.quest.QuestStatus
import com.lifequest.app.domain.shop.ItemEffectType
import com.lifequest.app.domain.shop.ShopCatalog
import com.lifequest.app.domain.world.WorldCatalog
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.temporal.TemporalAdjusters

/**
 * Luật chơi dạng hàm thuần: nhận GameState cũ, trả về GameState mới + các sự kiện.
 * Không phụ thuộc Android nên test được bằng JUnit thường.
 */
object GameEngine {

    fun newGame(): GameState = GameState()

    /**
     * Gọi khi mở app hoặc quay lại app. Sang ngày mới thì làm mới quest Daily,
     * sang tuần mới (thứ Hai) thì làm mới cả Weekly, bỏ lỡ một ngày thì mất streak.
     * `today` được truyền vào thay vì gọi LocalDate.now() để hàm vẫn thuần và test được.
     */
    fun startDay(state: GameState, today: LocalDate): GameOutcome {
        val todayEpoch = today.toEpochDay()
        if (state.lastActiveEpochDay >= todayEpoch) return GameOutcome(state)

        val firstLaunch = state.lastActiveEpochDay == GameState.NEVER
        val newWeek = firstLaunch || weekStart(today) != weekStart(LocalDate.ofEpochDay(state.lastActiveEpochDay))
        val resetIds = QuestCatalog.daily.map { it.id } + if (newWeek) QuestCatalog.weekly.map { it.id } else emptyList()
        val streakBroken = state.lastQuestEpochDay < todayEpoch - 1

        val newState = state.copy(
            quests = state.quests.filterNot { it.questId in resetIds },
            player = if (streakBroken) state.player.copy(streak = 0) else state.player,
            lastActiveEpochDay = todayEpoch,
        )
        return GameOutcome(newState, if (firstLaunch) emptyList() else listOf(GameEvent.NewDay(weeklyReset = newWeek)))
    }

    /** Hoàn thành quest: tự động nhận thưởng (XP, Gold), cộng streak và gây sát thương lên Boss. */
    fun completeQuest(state: GameState, questId: String, today: LocalDate): GameOutcome {
        val def = QuestCatalog.find(questId) ?: return GameOutcome(state, listOf(GameEvent.ActionFailed))
        if (state.isQuestDone(questId)) return GameOutcome(state)

        val quests = state.quests.filterNot { it.questId == questId } +
            QuestProgress(questId, QuestStatus.CLAIMED)

        val streaked = updateStreak(state.copy(quests = quests), today)
        val rewarded = grantReward(streaked.state, def.rewards.xp, def.rewards.gold)
        val events = streaked.events + rewarded.events
        if (def.rewards.bossDamage <= 0) return GameOutcome(rewarded.state, events)

        val damaged = damageCurrentBoss(rewarded.state, def.rewards.bossDamage)
        return GameOutcome(damaged.state, events + damaged.events)
    }

    fun damageCurrentBoss(state: GameState, damage: Int): GameOutcome {
        val world = state.currentWorldDef
        val progress = state.currentWorldProgress ?: return GameOutcome(state)
        if (progress.isCleared) return GameOutcome(state)

        val newHp = (progress.bossCurrentHp - damage).coerceAtLeast(0)
        val defeated = newHp == 0
        var worlds = state.worlds.map {
            if (it.worldId == world.id) it.copy(bossCurrentHp = newHp, isCleared = defeated) else it
        }
        if (!defeated) return GameOutcome(state.copy(worlds = worlds))

        // Boss gục: mở khóa world tiếp theo và ghi vào Legacy Monument
        WorldCatalog.worlds.getOrNull(state.currentWorldIndex + 1)?.let { next ->
            if (worlds.none { it.worldId == next.id }) worlds = worlds + WorldCatalog.initialProgress(next)
        }
        val player = state.player.copy(legacy = state.player.legacy + "Defeated ${world.boss.name} in ${world.name}")
        return GameOutcome(
            state.copy(worlds = worlds, player = player),
            listOf(GameEvent.BossDefeated(world.boss.name)),
        )
    }

    fun nextWorld(state: GameState): GameState {
        val cleared = state.currentWorldProgress?.isCleared == true
        return if (cleared && state.hasNextWorld) state.copy(currentWorldIndex = state.currentWorldIndex + 1) else state
    }

    fun buyItem(state: GameState, itemId: String): GameOutcome {
        val item = ShopCatalog.find(itemId) ?: return GameOutcome(state, listOf(GameEvent.ActionFailed))
        if (state.player.gold < item.price) return GameOutcome(state, listOf(GameEvent.NotEnoughGold))

        return GameOutcome(
            state.copy(
                player = state.player.copy(gold = state.player.gold - item.price),
                inventory = InventoryRules.add(state.inventory, itemId),
            ),
            listOf(GameEvent.ItemBought(item.name)),
        )
    }

    fun useItem(state: GameState, itemId: String): GameOutcome {
        val item = ShopCatalog.find(itemId)
        if (item == null || !InventoryRules.has(state.inventory, itemId)) {
            return GameOutcome(state, listOf(GameEvent.ActionFailed))
        }

        val consumed = state.copy(inventory = InventoryRules.remove(state.inventory, itemId))
        val effect = when (item.effect.type) {
            ItemEffectType.GRANT_XP -> grantReward(consumed, item.effect.value, 0)
            ItemEffectType.GRANT_GOLD -> grantReward(consumed, 0, item.effect.value)
            ItemEffectType.DAMAGE_BOSS -> damageCurrentBoss(consumed, item.effect.value)
        }
        return GameOutcome(effect.state, listOf(GameEvent.ItemUsed(item.name)) + effect.events)
    }

    // --- Dev console ---

    fun cheatXp(state: GameState): GameOutcome = grantReward(state, GameBalance.CHEAT_XP, 0)

    fun cheatGold(state: GameState): GameOutcome = grantReward(state, 0, GameBalance.CHEAT_GOLD)

    fun killBoss(state: GameState): GameOutcome =
        damageCurrentBoss(state, state.currentWorldProgress?.bossCurrentHp ?: 0)

    private fun updateStreak(state: GameState, today: LocalDate): GameOutcome {
        val todayEpoch = today.toEpochDay()
        val streak = when (state.lastQuestEpochDay) {
            todayEpoch -> return GameOutcome(state) // hôm nay đã tính rồi
            todayEpoch - 1 -> state.player.streak + 1
            else -> 1
        }
        val events = if (streak > 1) listOf(GameEvent.StreakExtended(streak)) else emptyList()
        return GameOutcome(
            state.copy(player = state.player.copy(streak = streak), lastQuestEpochDay = todayEpoch),
            events,
        )
    }

    private fun weekStart(date: LocalDate): LocalDate = date.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))

    private fun grantReward(state: GameState, xp: Int, gold: Int): GameOutcome {
        val result = PlayerRules.addReward(state.player, xp, gold)
        val events = if (result.levelUp) listOf(GameEvent.LevelUp(result.progress.level, result.progress.title)) else emptyList()
        return GameOutcome(state.copy(player = result.progress), events)
    }
}
