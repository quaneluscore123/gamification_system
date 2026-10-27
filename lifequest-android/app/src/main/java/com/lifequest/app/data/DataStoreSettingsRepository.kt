package com.lifequest.app.data

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.lifequest.app.domain.settings.SettingsRepository
import com.lifequest.app.domain.settings.UserSettings
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

private val Context.settingsStore by preferencesDataStore(name = "settings")

/** Cài đặt dạng key-value nhỏ: đúng chỗ của Preferences DataStore (dữ liệu lớn/có quan hệ thì dùng Room). */
class DataStoreSettingsRepository @Inject constructor(
    @ApplicationContext private val context: Context,
) : SettingsRepository {

    override val settings: Flow<UserSettings> = context.settingsStore.data.map { prefs ->
        UserSettings(
            reminderEnabled = prefs[KEY_REMINDER_ENABLED] ?: false,
            reminderHour = prefs[KEY_REMINDER_HOUR] ?: UserSettings.DEFAULT_REMINDER_HOUR,
        )
    }

    override suspend fun setReminderEnabled(enabled: Boolean) {
        context.settingsStore.edit { it[KEY_REMINDER_ENABLED] = enabled }
    }

    override suspend fun setReminderHour(hour: Int) {
        context.settingsStore.edit { it[KEY_REMINDER_HOUR] = hour.coerceIn(0, 23) }
    }

    private companion object {
        val KEY_REMINDER_ENABLED = booleanPreferencesKey("reminder_enabled")
        val KEY_REMINDER_HOUR = intPreferencesKey("reminder_hour")
    }
}
