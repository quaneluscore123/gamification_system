package com.lifequest.app.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.lifequest.app.LifeQuestApp
import com.lifequest.app.domain.game.GameRepository
import com.lifequest.app.domain.game.GameEngine
import com.lifequest.app.domain.game.GameEvent
import com.lifequest.app.domain.game.GameOutcome
import com.lifequest.app.domain.game.GameState
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

sealed interface GameUiState {
    data object Loading : GameUiState
    data class Ready(val game: GameState) : GameUiState
}

class GameViewModel(private val repository: GameRepository) : ViewModel() {

    private val _uiState = MutableStateFlow<GameUiState>(GameUiState.Loading)
    val uiState: StateFlow<GameUiState> = _uiState.asStateFlow()

    private val _events = Channel<GameEvent>(Channel.BUFFERED)
    val events: Flow<GameEvent> = _events.receiveAsFlow()

    init {
        viewModelScope.launch {
            _uiState.value = GameUiState.Ready(repository.load())
        }
    }

    fun completeQuest(questId: String) = dispatch { GameEngine.completeQuest(it, questId) }
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
        }
    }

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

    companion object {
        val Factory = viewModelFactory {
            initializer {
                val app = this[APPLICATION_KEY] as LifeQuestApp
                GameViewModel(app.container.gameRepository)
            }
        }
    }
}
