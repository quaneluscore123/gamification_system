package com.lifequest.app.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lifequest.app.domain.game.GameRepository
import com.lifequest.app.domain.game.GameEngine
import com.lifequest.app.domain.game.GameEvent
import com.lifequest.app.domain.game.GameOutcome
import com.lifequest.app.domain.game.GameState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import java.time.Clock
import java.time.LocalDate
import javax.inject.Inject

sealed interface GameUiState {
    data object Loading : GameUiState
    data class Ready(val game: GameState) : GameUiState
}

@HiltViewModel
class GameViewModel @Inject constructor(
    private val repository: GameRepository,
    private val clock: Clock,
) : ViewModel() {

    private val _uiState = MutableStateFlow<GameUiState>(GameUiState.Loading)
    val uiState: StateFlow<GameUiState> = _uiState.asStateFlow()

    private val _events = Channel<GameEvent>(Channel.BUFFERED)
    val events: Flow<GameEvent> = _events.receiveAsFlow()

    init {
        viewModelScope.launch {
            _uiState.value = GameUiState.Ready(repository.load())
            onAppResumed()
        }
    }

    /** Làm mới quest nếu đã sang ngày mới (kể cả khi app để mở qua đêm). */
    fun onAppResumed() = dispatch { GameEngine.startDay(it, today()) }

    fun completeQuest(questId: String) = dispatch { GameEngine.completeQuest(it, questId, today()) }
    fun buyItem(itemId: String) = dispatch { GameEngine.buyItem(it, itemId) }
    fun useItem(itemId: String) = dispatch { GameEngine.useItem(it, itemId) }
    fun nextWorld() = dispatch { GameOutcome(GameEngine.nextWorld(it)) }
    fun cheatXp() = dispatch(GameEngine::cheatXp)
    fun cheatGold() = dispatch(GameEngine::cheatGold)
    fun killBoss() = dispatch(GameEngine::killBoss)

    fun resetSave() {
        viewModelScope.launch {
            repository.clear()
            _uiState.value = GameUiState.Ready(GameEngine.newGame())
            onAppResumed()
        }
    }

    private fun today(): LocalDate = LocalDate.now(clock)

    /** Áp dụng một luật chơi lên state hiện tại, lưu lại và phát sự kiện cho UI. */
    private fun dispatch(action: (GameState) -> GameOutcome) {
        val current = (_uiState.value as? GameUiState.Ready)?.game ?: return
        val outcome = action(current)
        if (outcome.state != current) {
            _uiState.value = GameUiState.Ready(outcome.state)
            viewModelScope.launch { repository.save(outcome.state) }
        }
        outcome.events.forEach { _events.trySend(it) }
    }

}
