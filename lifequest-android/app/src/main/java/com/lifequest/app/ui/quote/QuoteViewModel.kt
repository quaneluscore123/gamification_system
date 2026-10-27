package com.lifequest.app.ui.quote

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lifequest.app.domain.quote.Quote
import com.lifequest.app.domain.quote.QuoteRepository
import com.lifequest.app.domain.quote.QuoteResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/** Mẫu Loading / Success / Error kinh điển cho màn hình gọi API. */
sealed interface QuoteUiState {
    data object Loading : QuoteUiState
    data class Success(val quote: Quote, val offline: Boolean) : QuoteUiState
    data class Error(val message: String) : QuoteUiState
}

@HiltViewModel
class QuoteViewModel @Inject constructor(
    private val repository: QuoteRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow<QuoteUiState>(QuoteUiState.Loading)
    val uiState: StateFlow<QuoteUiState> = _uiState.asStateFlow()

    private var loadJob: Job? = null

    init {
        load(forceRefresh = false)
    }

    fun refresh() = load(forceRefresh = true)

    private fun load(forceRefresh: Boolean) {
        // Bấm "Làm mới" liên tục: hủy request cũ, chỉ giữ request mới nhất
        loadJob?.cancel()
        loadJob = viewModelScope.launch {
            _uiState.value = QuoteUiState.Loading
            _uiState.value = when (val result = repository.getDailyQuote(forceRefresh)) {
                is QuoteResult.Success -> QuoteUiState.Success(result.quote, offline = result.fromCache)
                is QuoteResult.Failure -> QuoteUiState.Error(
                    when (result.reason) {
                        QuoteResult.Reason.NO_NETWORK -> "Không có kết nối mạng. Kiểm tra Wi-Fi rồi thử lại nhé."
                        QuoteResult.Reason.SERVER_ERROR -> "Máy chủ đang bận. Thử lại sau ít phút."
                    },
                )
            }
        }
    }
}
