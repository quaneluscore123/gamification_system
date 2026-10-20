package com.lifequest.app.ui.world

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.lifequest.app.domain.game.GameState
import com.lifequest.app.domain.quest.QuestCatalog
import com.lifequest.app.ui.components.ActionRow
import com.lifequest.app.ui.components.GameCard
import com.lifequest.app.ui.components.SectionTitle
import com.lifequest.app.ui.components.StatBar
import com.lifequest.app.ui.theme.LqColors

fun LazyListScope.worldSection(
    game: GameState,
    onFight: (questId: String) -> Unit,
    onNextWorld: () -> Unit,
) {
    val world = game.currentWorldDef
    val progress = game.currentWorldProgress ?: return

    item(key = "world_header") {
        GameCard(Modifier.padding(bottom = 8.dp)) {
            Text(
                "World ${game.currentWorldIndex + 1}: ${world.name}",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = LqColors.Primary,
            )
            Text(world.description, style = MaterialTheme.typography.bodySmall, color = LqColors.TextMuted)
        }
    }

    item(key = "boss") {
        GameCard(color = LqColors.Danger.copy(alpha = 0.12f)) {
            Text("⚔️ Boss: ${world.boss.name}", style = MaterialTheme.typography.titleSmall, color = LqColors.Danger)
            StatBar(
                progress.bossCurrentHp.toFloat() / world.boss.maxHp,
                LqColors.Danger,
                Modifier.padding(vertical = 6.dp),
            )
            Text("${progress.bossCurrentHp} / ${world.boss.maxHp} HP", style = MaterialTheme.typography.bodySmall)
        }
    }

    item(key = "rooms_title") { SectionTitle("Dungeon Rooms") }

    items(world.rooms, key = { it.id }) { room ->
        val rewards = QuestCatalog.find(room.questId)?.rewards
        val completed = game.isQuestDone(room.questId)
        ActionRow(
            title = room.name,
            subtitle = "+${rewards?.xp ?: 0} XP | +${rewards?.gold ?: 0} 💰",
            actionLabel = if (completed) "Cleared" else "Fight",
            enabled = !completed && !progress.isCleared,
            done = completed,
            onAction = { onFight(room.questId) },
        )
    }

    if (progress.isCleared) {
        item(key = "cleared") {
            Column(
                Modifier.fillMaxWidth().padding(top = 8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                Text(
                    "🎉 Vượt ải thành công!",
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    color = LqColors.Success,
                    fontWeight = FontWeight.Bold,
                )
                if (game.hasNextWorld) {
                    Button(
                        onClick = onNextWorld,
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = LqColors.Success),
                    ) { Text("Tiến vào World ${game.currentWorldIndex + 2} ➜") }
                } else {
                    Text(
                        "Bạn đã chinh phục tất cả các World hiện có!",
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center,
                        color = LqColors.TextMuted,
                    )
                }
            }
        }
    }
}
