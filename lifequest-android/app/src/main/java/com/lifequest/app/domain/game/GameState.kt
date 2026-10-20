package com.lifequest.app.domain.game

import com.lifequest.app.domain.inventory.InventoryItem
import com.lifequest.app.domain.player.PlayerProgress
import com.lifequest.app.domain.quest.QuestProgress
import com.lifequest.app.domain.quest.QuestStatus
import com.lifequest.app.domain.quest.isDone
import com.lifequest.app.domain.world.WorldCatalog
import com.lifequest.app.domain.world.WorldDef
import com.lifequest.app.domain.world.WorldProgress
import kotlinx.serialization.Serializable

/** Toàn bộ tiến trình của người chơi — đây là thứ được lưu xuống máy. */
@Serializable
data class GameState(
    val player: PlayerProgress = PlayerProgress(),
    val quests: List<QuestProgress> = emptyList(),
    val worlds: List<WorldProgress> = listOf(WorldCatalog.initialProgress(WorldCatalog.worlds.first())),
    val currentWorldIndex: Int = 0,
    val inventory: List<InventoryItem> = emptyList(),
) {
    val currentWorldDef: WorldDef
        get() = WorldCatalog.worlds[currentWorldIndex.coerceIn(WorldCatalog.worlds.indices)]

    val currentWorldProgress: WorldProgress?
        get() = worlds.find { it.worldId == currentWorldDef.id }

    val hasNextWorld: Boolean
        get() = currentWorldIndex < WorldCatalog.worlds.lastIndex

    fun questStatus(questId: String): QuestStatus? =
        quests.find { it.questId == questId }?.status

    fun isQuestDone(questId: String): Boolean = questStatus(questId)?.isDone == true
}

/** Sự kiện để UI hiển thị thông báo (thay cho alert() bên bản web). */
sealed interface GameEvent {
    data class LevelUp(val level: Int, val title: String) : GameEvent
    data class BossDefeated(val bossName: String) : GameEvent
    data class ItemBought(val itemName: String) : GameEvent
    data class ItemUsed(val itemName: String) : GameEvent
    data object NotEnoughGold : GameEvent
    data object ActionFailed : GameEvent
}

data class GameOutcome(
    val state: GameState,
    val events: List<GameEvent> = emptyList(),
)
