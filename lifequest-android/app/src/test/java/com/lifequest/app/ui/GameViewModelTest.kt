package com.lifequest.app.ui

import app.cash.turbine.test
import com.lifequest.app.domain.game.GameEvent
import com.lifequest.app.domain.game.GameState
import com.lifequest.app.domain.quest.QuestProgress
import com.lifequest.app.domain.quest.QuestStatus
import com.lifequest.app.testing.FakeAnalyticsTracker
import com.lifequest.app.testing.FakeGameRepository
import com.lifequest.app.testing.FakeHistoryRepository
import com.lifequest.app.testing.MainDispatcherRule
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import java.time.Clock
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneOffset

class GameViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    // Đồng hồ cố định: test luôn chạy như thể hôm nay là 27/10/2026
    private val clock = Clock.fixed(Instant.parse("2026-10-27T10:00:00Z"), ZoneOffset.UTC)
    private val today = LocalDate.now(clock)

    private val repository = FakeGameRepository()
    private val history = FakeHistoryRepository()
    private val analytics = FakeAnalyticsTracker()

    private fun createViewModel() = GameViewModel(repository, history, analytics, clock)

    private val GameViewModel.game: GameState
        get() = (uiState.value as GameUiState.Ready).game

    @Test
    fun `loads the saved game on start`() {
        val viewModel = createViewModel()

        assertTrue(viewModel.uiState.value is GameUiState.Ready)
        assertEquals(today.toEpochDay(), viewModel.game.lastActiveEpochDay)
    }

    @Test
    fun `completing a quest saves state, records history and logs analytics`() = runTest {
        val viewModel = createViewModel()

        viewModel.completeQuest("dq_1")

        assertTrue(viewModel.game.isQuestDone("dq_1"))
        assertTrue(repository.saved.isQuestDone("dq_1"))
        assertEquals(listOf("dq_1"), history.logs.value.map { it.questId })
        assertTrue("quest_complete" in analytics.events)
    }

    @Test
    fun `completing the same quest twice records history only once`() = runTest {
        val viewModel = createViewModel()

        viewModel.completeQuest("dq_1")
        viewModel.completeQuest("dq_1")

        assertEquals(1, history.logs.value.size)
    }

    @Test
    fun `buying without gold emits NotEnoughGold`() = runTest {
        val viewModel = createViewModel()

        // Turbine: lắng nghe Flow và lấy từng giá trị bằng awaitItem()
        viewModel.events.test {
            viewModel.buyItem("itm_potion_xp")
            assertEquals(GameEvent.NotEnoughGold, awaitItem())
        }
    }

    @Test
    fun `a save from yesterday gets its daily quests reset on start`() {
        repository.saved = GameState(
            quests = listOf(QuestProgress("dq_1", QuestStatus.CLAIMED)),
            lastActiveEpochDay = today.minusDays(1).toEpochDay(),
        )

        val viewModel = createViewModel()

        assertFalse(viewModel.game.isQuestDone("dq_1"))
    }

    @Test
    fun `reset save clears game and history`() = runTest {
        val viewModel = createViewModel()
        viewModel.completeQuest("dq_1")

        viewModel.resetSave()

        assertFalse(viewModel.game.isQuestDone("dq_1"))
        assertTrue(history.logs.value.isEmpty())
    }
}
