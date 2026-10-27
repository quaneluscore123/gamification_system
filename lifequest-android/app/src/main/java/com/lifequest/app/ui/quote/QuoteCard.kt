package com.lifequest.app.ui.quote

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.lifequest.app.domain.quote.Quote
import com.lifequest.app.ui.components.GameCard
import com.lifequest.app.ui.theme.LifeQuestTheme
import com.lifequest.app.ui.theme.LqColors

@Composable
fun QuoteCard(state: QuoteUiState, onRefresh: () -> Unit, modifier: Modifier = Modifier) {
    GameCard(modifier.padding(bottom = 8.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("💬 Quote of the day", style = MaterialTheme.typography.titleSmall, color = LqColors.Primary)
            Spacer(Modifier.weight(1f))
            TextButton(onClick = onRefresh, enabled = state !is QuoteUiState.Loading) {
                Text(if (state is QuoteUiState.Error) "Thử lại" else "Làm mới")
            }
        }

        // when trên sealed interface: thiếu nhánh nào compiler báo lỗi ngay
        when (state) {
            QuoteUiState.Loading -> Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                CircularProgressIndicator(Modifier.size(18.dp), strokeWidth = 2.dp)
                Text("Đang tải...", style = MaterialTheme.typography.bodySmall, color = LqColors.TextMuted)
            }
            is QuoteUiState.Success -> {
                Text("“${state.quote.text}”", style = MaterialTheme.typography.bodyMedium, fontStyle = FontStyle.Italic)
                Text(
                    "— ${state.quote.author}" + if (state.offline) "  (offline)" else "",
                    modifier = Modifier.padding(top = 4.dp),
                    style = MaterialTheme.typography.bodySmall,
                    color = LqColors.TextMuted,
                )
            }
            is QuoteUiState.Error -> Text(state.message, style = MaterialTheme.typography.bodySmall, color = LqColors.Danger)
        }
    }
}

@Preview
@Composable
private fun QuoteCardPreview() {
    LifeQuestTheme {
        QuoteCard(QuoteUiState.Success(Quote("Talk is cheap. Show me the code.", "Linus Torvalds"), offline = true), onRefresh = {})
    }
}
