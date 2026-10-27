package com.lifequest.app.ui.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lifequest.app.domain.settings.SettingsRepository
import com.lifequest.app.domain.settings.UserSettings
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val repository: SettingsRepository,
) : ViewModel() {

    val settings: StateFlow<UserSettings> =
        repository.settings.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), UserSettings())

    fun setReminderEnabled(enabled: Boolean) {
        viewModelScope.launch { repository.setReminderEnabled(enabled) }
    }

    fun changeReminderHour(delta: Int) {
        val hour = Math.floorMod(settings.value.reminderHour + delta, 24)
        viewModelScope.launch { repository.setReminderHour(hour) }
    }
}
