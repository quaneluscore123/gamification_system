package com.lifequest.app.ui.quest

import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import com.lifequest.app.domain.game.GameState
import com.lifequest.app.domain.quest.QuestCatalog
import com.lifequest.app.domain.quest.QuestDef
import com.lifequest.app.ui.components.ActionRow
import com.lifequest.app.ui.components.SectionTitle

fun LazyListScope.questSection(game: GameState, onComplete: (questId: String) -> Unit) {
    questGroup("Nhiệm Vụ Hàng Ngày (Daily)", QuestCatalog.daily, game, onComplete)
    questGroup("Nhiệm Vụ Tuần (Weekly)", QuestCatalog.weekly, game, onComplete)
}

private fun LazyListScope.questGroup(
    title: String,
    quests: List<QuestDef>,
    game: GameState,
    onComplete: (questId: String) -> Unit,
) {
    item(key = "title_$title") { SectionTitle(title) }
    items(quests, key = { it.id }) { quest ->
        val done = game.isQuestDone(quest.id)
        ActionRow(
            title = quest.title,
            detail = "⏳ ${quest.estimatedTimeMin}m | ${quest.tags.joinToString(", ")}",
            subtitle = "+${quest.rewards.xp} XP | +${quest.rewards.gold} 💰 | ⚔️ ${quest.rewards.bossDamage} DMG",
            actionLabel = if (done) "Đã Xong" else "Hoàn Thành",
            enabled = !done,
            done = done,
            onAction = { onComplete(quest.id) },
        )
    }
}
