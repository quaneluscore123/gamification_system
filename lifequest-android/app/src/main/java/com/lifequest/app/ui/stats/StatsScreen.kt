package com.lifequest.app.ui.stats

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.lifequest.app.domain.history.HistoryTotals
import com.lifequest.app.domain.history.QuestLog
import com.lifequest.app.domain.player.PlayerProgress
import com.lifequest.app.ui.components.GameCard
import com.lifequest.app.ui.components.SectionTitle
import com.lifequest.app.ui.theme.LifeQuestTheme
import com.lifequest.app.ui.theme.LqColors
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

/** Stateful: lấy ViewModel và state, rồi giao cho bản stateless bên dưới vẽ. */
@Composable
fun StatsScreen(
    player: PlayerProgress,
    modifier: Modifier = Modifier,
    viewModel: StatsViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    StatsContent(player, uiState, modifier)
}

/** Stateless: chỉ nhận dữ liệu → dễ @Preview và dễ viết UI test. */
@Composable
fun StatsContent(player: PlayerProgress, uiState: StatsUiState, modifier: Modifier = Modifier) {
    LazyColumn(modifier = modifier, contentPadding = PaddingValues(16.dp)) {
        item(key = "title") { SectionTitle("📊 Thống kê") }
        item(key = "tiles_1") {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                StatTile("✅", "Quest đã xong", uiState.totals.questCount.toString(), Modifier.weight(1f))
                StatTile("🔥", "Streak", "${player.streak} ngày", Modifier.weight(1f))
            }
        }
        item(key = "tiles_2") {
            Row(Modifier.padding(top = 8.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                StatTile("⭐", "Tổng XP", uiState.totals.totalXp.toString(), Modifier.weight(1f))
                StatTile("💰", "Tổng Gold", uiState.totals.totalGold.toString(), Modifier.weight(1f))
            }
        }

        item(key = "legacy_title") { SectionTitle("🏛️ Legacy Monument", Modifier.padding(top = 8.dp)) }
        if (player.legacy.isEmpty()) {
            item(key = "legacy_empty") { MutedText("Chưa hạ Boss nào. Vào Dungeons chiến đấu thôi!") }
        } else {
            items(player.legacy, key = { it }) { entry -> Text("🏆 $entry", Modifier.padding(vertical = 2.dp)) }
        }

        item(key = "history_title") { SectionTitle("🕑 Lịch sử gần đây", Modifier.padding(top = 8.dp)) }
        if (uiState.recent.isEmpty()) {
            item(key = "history_empty") { MutedText("Chưa có quest nào được hoàn thành.") }
        } else {
            items(uiState.recent, key = { "${it.completedAtMillis}_${it.questId}" }) { log -> HistoryRow(log) }
        }
    }
}

@Composable
private fun StatTile(icon: String, label: String, value: String, modifier: Modifier = Modifier) {
    GameCard(modifier) {
        Text(icon, style = MaterialTheme.typography.titleLarge)
        Text(value, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
        Text(label, style = MaterialTheme.typography.bodySmall, color = LqColors.TextMuted)
    }
}

@Composable
private fun HistoryRow(log: QuestLog) {
    GameCard(Modifier.padding(bottom = 6.dp)) {
        Text(log.title, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.SemiBold)
        Text(
            "${timeFormatter.format(Instant.ofEpochMilli(log.completedAtMillis))}  •  +${log.xp} XP  •  +${log.gold} 💰",
            style = MaterialTheme.typography.bodySmall,
            color = LqColors.TextMuted,
        )
    }
}

@Composable
private fun MutedText(text: String) {
    Text(text, style = MaterialTheme.typography.bodySmall, color = LqColors.TextMuted)
}

private val timeFormatter = DateTimeFormatter.ofPattern("dd/MM HH:mm").withZone(ZoneId.systemDefault())

@Preview
@Composable
private fun StatsContentPreview() {
    LifeQuestTheme {
        StatsContent(
            player = PlayerProgress(streak = 3, legacy = listOf("Defeated OOP Master in Java Fundamentals")),
            uiState = StatsUiState(
                totals = HistoryTotals(questCount = 12, totalXp = 640, totalGold = 230),
                recent = listOf(QuestLog("dq_1", "Write 50 lines of code", 50, 10, 1_793_000_000_000)),
            ),
        )
    }
}
