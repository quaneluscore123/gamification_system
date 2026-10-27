package com.lifequest.app.testing

import com.lifequest.app.domain.analytics.AnalyticsTracker
import com.lifequest.app.domain.game.GameEngine
import com.lifequest.app.domain.game.GameRepository
import com.lifequest.app.domain.game.GameState
import com.lifequest.app.domain.history.HistoryRepository
import com.lifequest.app.domain.history.HistoryTotals
import com.lifequest.app.domain.history.QuestLog
import com.lifequest.app.domain.quote.QuoteRepository
import com.lifequest.app.domain.quote.QuoteResult
import com.lifequest.app.domain.settings.ReminderScheduler
import com.lifequest.app.domain.settings.SettingsRepository
import com.lifequest.app.domain.settings.UserSettings
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map

/*
 * Fake = bản cài đặt thật nhưng đơn giản (lưu trong RAM) của các interface ở domain.
 * Nhờ ViewModel chỉ phụ thuộc interface, test không cần DataStore, Room, Retrofit hay WorkManager.
 */

class FakeGameRepository(var saved: GameState = GameEngine.newGame()) : GameRepository {
    override suspend fun load() = saved
    override suspend fun save(state: GameState) {
        saved = state
    }
    override suspend fun clear() {
        saved = GameEngine.newGame()
    }
}

class FakeHistoryRepository : HistoryRepository {
    val logs = MutableStateFlow<List<QuestLog>>(emptyList())

    override fun observeRecent(limit: Int): Flow<List<QuestLog>> = logs.map { it.takeLast(limit).reversed() }
    override fun observeTotals(): Flow<HistoryTotals> =
        logs.map { list -> HistoryTotals(list.size, list.sumOf { it.xp }, list.sumOf { it.gold }) }
    override suspend fun record(log: QuestLog) {
        logs.value += log
    }
    override suspend fun clear() {
        logs.value = emptyList()
    }
}

class FakeAnalyticsTracker : AnalyticsTracker {
    val events = mutableListOf<String>()

    override val backendName = "Fake"
    override fun logEvent(name: String, params: Map<String, Any>) {
        events += name
    }
    override fun recordNonFatal(error: Throwable) = Unit
}

class FakeQuoteRepository(var result: QuoteResult) : QuoteRepository {
    var calls = 0
    override suspend fun getDailyQuote(forceRefresh: Boolean): QuoteResult {
        calls++
        return result
    }
}

class FakeSettingsRepository : SettingsRepository {
    private val state = MutableStateFlow(UserSettings())

    override val settings: Flow<UserSettings> = state
    override suspend fun setReminderEnabled(enabled: Boolean) {
        state.value = state.value.copy(reminderEnabled = enabled)
    }
    override suspend fun setReminderHour(hour: Int) {
        state.value = state.value.copy(reminderHour = hour)
    }
}

class FakeReminderScheduler : ReminderScheduler {
    var scheduledHour: Int? = null

    override fun schedule(hour: Int) {
        scheduledHour = hour
    }
    override fun cancel() {
        scheduledHour = null
    }
}
