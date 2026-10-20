package com.lifequest.app.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.lifequest.app.domain.player.PlayerProgress
import com.lifequest.app.domain.player.PlayerRules
import com.lifequest.app.ui.theme.LifeQuestTheme
import com.lifequest.app.ui.theme.LqColors

@Composable
fun MentorAstra(level: Int, modifier: Modifier = Modifier) {
    GameCard(modifier) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Avatar("🧙‍♀️")
            Spacer(Modifier.width(12.dp))
            Text(
                text = "Astra: ${PlayerRules.mentorDialogue(level)}",
                style = MaterialTheme.typography.bodyMedium,
            )
        }
    }
}

@Composable
fun CharacterCard(player: PlayerProgress, modifier: Modifier = Modifier) {
    val requiredXp = PlayerRules.requiredXpForLevel(player.level)

    GameCard(modifier) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Avatar("👨‍💻")
            Spacer(Modifier.width(12.dp))
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text("Astra's Disciple", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Text("Lv. ${player.level} | ${player.title}", color = LqColors.Primary, style = MaterialTheme.typography.bodyMedium)
                StatBar(player.xp.toFloat() / requiredXp, LqColors.Primary)
                Text(
                    "XP: ${player.xp} / $requiredXp    💰 Gold: ${player.gold}",
                    style = MaterialTheme.typography.bodySmall,
                    color = LqColors.TextMuted,
                )
            }
        }
    }
}

@Composable
private fun Avatar(emoji: String) {
    Box(
        modifier = Modifier.size(52.dp).background(LqColors.CardRaised, CircleShape),
        contentAlignment = Alignment.Center,
    ) {
        Text(emoji, style = MaterialTheme.typography.headlineSmall)
    }
}

@Preview
@Composable
private fun CharacterCardPreview() {
    LifeQuestTheme {
        CharacterCard(PlayerProgress(level = 3, xp = 80, gold = 120, title = "Beginner"))
    }
}
