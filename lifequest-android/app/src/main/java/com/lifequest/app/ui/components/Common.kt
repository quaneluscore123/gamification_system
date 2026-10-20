package com.lifequest.app.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.lifequest.app.ui.theme.LqColors

@Composable
fun GameCard(
    modifier: Modifier = Modifier,
    color: Color = LqColors.Card,
    content: @Composable ColumnScope.() -> Unit,
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        color = color,
    ) {
        Column(Modifier.padding(14.dp), content = content)
    }
}

@Composable
fun StatBar(fraction: Float, color: Color, modifier: Modifier = Modifier) {
    LinearProgressIndicator(
        progress = { fraction.coerceIn(0f, 1f) },
        modifier = modifier.fillMaxWidth().height(10.dp),
        color = color,
        trackColor = LqColors.CardRaised,
        strokeCap = StrokeCap.Round,
        gapSize = 0.dp,
        drawStopIndicator = {},
    )
}

@Composable
fun SectionTitle(text: String, modifier: Modifier = Modifier) {
    Text(
        text = text,
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.SemiBold,
        modifier = modifier.padding(top = 8.dp, bottom = 8.dp),
    )
}

/** Một dòng gồm icon (tùy chọn), thông tin và nút hành động — dùng chung cho room, quest, item. */
@Composable
fun ActionRow(
    title: String,
    subtitle: String,
    actionLabel: String,
    enabled: Boolean,
    onAction: () -> Unit,
    modifier: Modifier = Modifier,
    done: Boolean = false,
    icon: String? = null,
    detail: String? = null,
) {
    GameCard(modifier.padding(bottom = 8.dp).alpha(if (done) 0.6f else 1f)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            if (icon != null) {
                Text(icon, style = MaterialTheme.typography.headlineMedium)
                Spacer12()
            }
            Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(2.dp)) {
                Text(title, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.SemiBold)
                if (detail != null) {
                    Text(detail, style = MaterialTheme.typography.bodySmall, color = LqColors.TextMuted)
                }
                Text(subtitle, style = MaterialTheme.typography.bodySmall, color = LqColors.Gold)
            }
            Spacer12()
            Button(
                onClick = onAction,
                enabled = enabled,
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (done) LqColors.Success else LqColors.Primary,
                    disabledContainerColor = LqColors.CardRaised,
                ),
            ) { Text(actionLabel) }
        }
    }
}

@Composable
private fun Spacer12() = Spacer(Modifier.width(12.dp))
