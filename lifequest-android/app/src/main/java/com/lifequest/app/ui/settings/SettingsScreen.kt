package com.lifequest.app.ui.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.lifequest.app.BuildConfig
import com.lifequest.app.domain.settings.UserSettings
import com.lifequest.app.ui.components.GameCard
import com.lifequest.app.ui.components.SectionTitle
import com.lifequest.app.ui.theme.LifeQuestTheme
import com.lifequest.app.ui.theme.LqColors

@Composable
fun SettingsScreen(modifier: Modifier = Modifier, viewModel: SettingsViewModel = hiltViewModel()) {
    val settings by viewModel.settings.collectAsStateWithLifecycle()
    SettingsContent(
        settings = settings,
        onReminderToggle = viewModel::setReminderEnabled,
        onHourChange = viewModel::changeReminderHour,
        modifier = modifier,
    )
}

@Composable
fun SettingsContent(
    settings: UserSettings,
    onReminderToggle: (Boolean) -> Unit,
    onHourChange: (delta: Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        SectionTitle("🔔 Nhắc nhở")
        GameCard {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Column(Modifier.weight(1f)) {
                    Text("Nhắc làm Quest mỗi ngày", fontWeight = FontWeight.SemiBold)
                    Text(
                        "Gửi thông báo để giữ chuỗi streak 🔥",
                        style = MaterialTheme.typography.bodySmall,
                        color = LqColors.TextMuted,
                    )
                }
                Switch(checked = settings.reminderEnabled, onCheckedChange = onReminderToggle)
            }
            if (settings.reminderEnabled) {
                Row(
                    Modifier.padding(top = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    Text("Giờ nhắc:", Modifier.weight(1f))
                    OutlinedButton(onClick = { onHourChange(-1) }) { Text("−") }
                    Text("%02d:00".format(settings.reminderHour), style = MaterialTheme.typography.titleMedium)
                    OutlinedButton(onClick = { onHourChange(+1) }) { Text("+") }
                }
            }
        }

        SectionTitle("ℹ️ Thông tin")
        GameCard {
            Text("LifeQuest ${BuildConfig.VERSION_NAME} (${BuildConfig.VERSION_CODE})")
            Text(
                if (BuildConfig.DEBUG) "Bản debug" else "Bản release",
                style = MaterialTheme.typography.bodySmall,
                color = LqColors.TextMuted,
            )
        }
    }
}

@Preview
@Composable
private fun SettingsContentPreview() {
    LifeQuestTheme {
        SettingsContent(UserSettings(reminderEnabled = true, reminderHour = 21), onReminderToggle = {}, onHourChange = {})
    }
}
