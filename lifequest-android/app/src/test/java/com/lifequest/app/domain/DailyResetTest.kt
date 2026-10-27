package com.lifequest.app.domain

import com.lifequest.app.domain.game.GameEngine
import com.lifequest.app.domain.game.GameEvent
import com.lifequest.app.domain.game.GameState
import com.lifequest.app.domain.player.PlayerProgress
import com.lifequest.app.domain.quest.QuestProgress
import com.lifequest.app.domain.quest.QuestStatus
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertSame
import org.junit.Assert.assertTrue
import org.junit.Test
import java.time.LocalDate

class DailyResetTest {

    // 2026-10-27 là thứ Ba
    private val tuesday = LocalDate.of(2026, 10, 27)

    private val doneDailyAndWeekly = GameState(
        quests = listOf(
            QuestProgress("dq_1", QuestStatus.CLAIMED),
            QuestProgress("wq_1", QuestStatus.CLAIMED),
            QuestProgress("q_w1_r1", QuestStatus.CLAIMED),
        ),
        lastActiveEpochDay = tuesday.toEpochDay(),
    )

    @Test
    fun `same day does nothing`() {
        val outcome = GameEngine.startDay(doneDailyAndWeekly, tuesday)

        assertSame(doneDailyAndWeekly, outcome.state)
        assertTrue(outcome.events.isEmpty())
    }

    @Test
    fun `next day resets daily quests but keeps weekly and main quests`() {
        val outcome = GameEngine.startDay(doneDailyAndWeekly, tuesday.plusDays(1))

        assertFalse(outcome.state.isQuestDone("dq_1"))
        assertTrue(outcome.state.isQuestDone("wq_1"))
        assertTrue(outcome.state.isQuestDone("q_w1_r1"))
        assertEquals(listOf(GameEvent.NewDay(weeklyReset = false)), outcome.events)
    }

    @Test
    fun `next monday also resets weekly quests`() {
        val monday = LocalDate.of(2026, 11, 2)
        val outcome = GameEngine.startDay(doneDailyAndWeekly, monday)

        assertFalse(outcome.state.isQuestDone("dq_1"))
        assertFalse(outcome.state.isQuestDone("wq_1"))
        assertTrue(outcome.state.isQuestDone("q_w1_r1"))
        assertEquals(listOf(GameEvent.NewDay(weeklyReset = true)), outcome.events)
    }

    @Test
    fun `first launch sets the active day without a new day message`() {
        val outcome = GameEngine.startDay(GameEngine.newGame(), tuesday)

        assertEquals(tuesday.toEpochDay(), outcome.state.lastActiveEpochDay)
        assertTrue(outcome.events.isEmpty())
    }

    @Test
    fun `completing quests on consecutive days extends the streak`() {
        val day1 = GameEngine.completeQuest(GameEngine.newGame(), "dq_1", tuesday).state
        assertEquals(1, day1.player.streak)

        val day2Start = GameEngine.startDay(day1, tuesday.plusDays(1)).state
        val day2 = GameEngine.completeQuest(day2Start, "dq_1", tuesday.plusDays(1))

        assertEquals(2, day2.state.player.streak)
        assertTrue(GameEvent.StreakExtended(2) in day2.events)
    }

    @Test
    fun `second quest on the same day does not increase the streak`() {
        val first = GameEngine.completeQuest(GameEngine.newGame(), "dq_1", tuesday).state
        val second = GameEngine.completeQuest(first, "dq_2", tuesday).state

        assertEquals(1, second.player.streak)
    }

    @Test
    fun `missing a day resets the streak to zero`() {
        val onStreak = GameState(
            player = PlayerProgress(streak = 5),
            lastActiveEpochDay = tuesday.toEpochDay(),
            lastQuestEpochDay = tuesday.toEpochDay(),
        )

        val twoDaysLater = GameEngine.startDay(onStreak, tuesday.plusDays(2)).state

        assertEquals(0, twoDaysLater.player.streak)
    }
}
