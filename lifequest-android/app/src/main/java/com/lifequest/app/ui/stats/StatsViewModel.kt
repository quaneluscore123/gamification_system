package com.lifequest.app.ui.stats

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lifequest.app.domain.history.HistoryRepository
import com.lifequest.app.domain.history.HistoryTotals
import com.lifequest.app.domain.history.QuestLog
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

data class StatsUiState(
    val totals: HistoryTotals = HistoryTotals(),
    val recent: List<QuestLog> = emptyList(),
)

@HiltViewModel
class StatsViewModel @Inject constructor(
    history: HistoryRepository,
) : ViewModel() {

    /**
     * combine: gộp 2 Flow từ Room, phát state mới khi một trong hai thay đổi.
     * stateIn + WhileSubscribed(5000): ngừng lắng nghe database 5 giây sau khi
     * màn hình biến mất, nhưng xoay màn hình (vài trăm ms) thì không phải truy vấn lại.
     */
    val uiState: StateFlow<StatsUiState> =
        combine(history.observeTotals(), history.observeRecent(RECENT_LIMIT)) { totals, recent ->
            StatsUiState(totals, recent)
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), StatsUiState())

    private companion object {
        const val RECENT_LIMIT = 20
    }
}
