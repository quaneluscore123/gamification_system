package com.lifequest.app.domain.game

import com.lifequest.app.domain.inventory.InventoryRules
import com.lifequest.app.domain.player.PlayerRules
import com.lifequest.app.domain.quest.QuestCatalog
import com.lifequest.app.domain.quest.QuestProgress
import com.lifequest.app.domain.quest.QuestStatus
import com.lifequest.app.domain.shop.ItemEffectType
import com.lifequest.app.domain.shop.ShopCatalog
import com.lifequest.app.domain.world.WorldCatalog

/**
 * Luật chơi dạng hàm thuần: nhận GameState cũ, trả về GameState mới + các sự kiện.
 * Không phụ thuộc Android nên test được bằng JUnit thường.
 */
object GameEngine {

    fun newGame(): GameState = GameState()

    /** Hoàn thành quest: tự động nhận thưởng (XP, Gold) và gây sát thương lên Boss. */
    fun completeQuest(state: GameState, questId: String): GameOutcome {
        val def = QuestCatalog.find(questId) ?: return GameOutcome(state, listOf(GameEvent.ActionFailed))
        if (state.isQuestDone(questId)) return GameOutcome(state)

        val quests = state.quests.filterNot { it.questId == questId } +
            QuestProgress(questId, QuestStatus.CLAIMED)

        val rewarded = grantReward(state.copy(quests = quests), def.rewards.xp, def.rewards.gold)
        if (def.rewards.bossDamage <= 0) return rewarded

        val damaged = damageCurrentBoss(rewarded.state, def.rewards.bossDamage)
        return GameOutcome(damaged.state, rewarded.events + damaged.events)
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

    private fun grantReward(state: GameState, xp: Int, gold: Int): GameOutcome {
        val result = PlayerRules.addReward(state.player, xp, gold)
        val events = if (result.levelUp) listOf(GameEvent.LevelUp(result.progress.level, result.progress.title)) else emptyList()
        return GameOutcome(state.copy(player = result.progress), events)
    }
}
