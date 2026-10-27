package com.lifequest.app.domain.settings

import kotlinx.coroutines.flow.Flow

data class UserSettings(
    val reminderEnabled: Boolean = false,
    /** Giờ nhắc trong ngày, 0..23. */
    val reminderHour: Int = DEFAULT_REMINDER_HOUR,
) {
    companion object {
        const val DEFAULT_REMINDER_HOUR = 20
    }
}

interface SettingsRepository {
    val settings: Flow<UserSettings>
    suspend fun setReminderEnabled(enabled: Boolean)
    suspend fun setReminderHour(hour: Int)
}
