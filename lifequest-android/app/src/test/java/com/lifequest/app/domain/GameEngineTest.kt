package com.lifequest.app.domain

import com.lifequest.app.domain.game.GameEngine
import com.lifequest.app.domain.game.GameEvent
import com.lifequest.app.domain.game.GameState
import com.lifequest.app.domain.inventory.InventoryItem
import com.lifequest.app.domain.player.PlayerProgress
import com.lifequest.app.domain.quest.QuestStatus
import com.lifequest.app.domain.world.WorldCatalog
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertSame
import org.junit.Assert.assertTrue
import org.junit.Test
import java.time.LocalDate

class GameEngineTest {

    private val newGame = GameEngine.newGame()
    private val today = LocalDate.of(2026, 10, 27)

    @Test
    fun `new game starts in world 1 with full boss hp`() {
        assertEquals(0, newGame.currentWorldIndex)
        assertEquals(500, newGame.currentWorldProgress?.bossCurrentHp)
    }

    @Test
    fun `completing a quest grants rewards, damages boss and marks it claimed`() {
        val outcome = GameEngine.completeQuest(newGame, "q_w1_r1", today)
        val state = outcome.state

        assertEquals(50, state.player.xp)
        assertEquals(20, state.player.gold)
        assertEquals(400, state.currentWorldProgress?.bossCurrentHp)
        assertEquals(QuestStatus.CLAIMED, state.questStatus("q_w1_r1"))
    }

    @Test
    fun `completing the same quest twice gives no extra reward`() {
        val once = GameEngine.completeQuest(newGame, "dq_1", today).state
        val twice = GameEngine.completeQuest(once, "dq_1", today)

        assertSame(once, twice.state)
        assertTrue(twice.events.isEmpty())
    }

    @Test
    fun `clearing every room of world 1 defeats the boss and unlocks world 2`() {
        var state = newGame
        listOf("q_w1_r1", "q_w1_r2", "q_w1_r3", "q_w1_r4", "q_w1_r5").forEach {
            state = GameEngine.completeQuest(state, it, today).state
        }

        assertTrue(state.currentWorldProgress!!.isCleared)
        assertTrue(state.worlds.any { it.worldId == "world_2" })
        assertEquals(listOf("Defeated OOP Master in Java Fundamentals"), state.player.legacy)

        val moved = GameEngine.nextWorld(state)
        assertEquals(1, moved.currentWorldIndex)
        assertEquals(800, moved.currentWorldProgress?.bossCurrentHp)
    }

    @Test
    fun `clearing every room of world 4 defeats the boss`() {
        var state = newGame.copy(
            currentWorldIndex = 3,
            worlds = listOf(WorldCatalog.initialProgress(WorldCatalog.worlds[3])),
        )
        listOf("q_w4_r1", "q_w4_r2", "q_w4_r3", "q_w4_r4").forEach {
            state = GameEngine.completeQuest(state, it, today).state
        }

        assertTrue(state.currentWorldProgress!!.isCleared)
        assertEquals(0, state.currentWorldProgress?.bossCurrentHp)
    }

    @Test
    fun `cannot move to next world before boss is defeated`() {
        assertEquals(0, GameEngine.nextWorld(newGame).currentWorldIndex)
    }

    @Test
    fun `buying without enough gold fails and keeps state`() {
        val outcome = GameEngine.buyItem(newGame, "itm_potion_xp")

        assertSame(newGame, outcome.state)
        assertEquals(listOf(GameEvent.NotEnoughGold), outcome.events)
    }

    @Test
    fun `buying an item spends gold and adds it to inventory`() {
        val rich = newGame.copy(player = PlayerProgress(gold = 200))
        val state = GameEngine.buyItem(rich, "itm_potion_xp").state

        assertEquals(150, state.player.gold)
        assertEquals(listOf(InventoryItem("itm_potion_xp", 1)), state.inventory)
    }

    @Test
    fun `using sword damages current boss and consumes the item`() {
        val armed = newGame.copy(inventory = listOf(InventoryItem("itm_sword_clean_code", 2)))
        val state = GameEngine.useItem(armed, "itm_sword_clean_code").state

        assertEquals(300, state.currentWorldProgress?.bossCurrentHp)
        assertEquals(listOf(InventoryItem("itm_sword_clean_code", 1)), state.inventory)
    }

    @Test
    fun `using last potion removes it and may level up`() {
        val state = GameState(inventory = listOf(InventoryItem("itm_potion_xp", 1)))
        val outcome = GameEngine.useItem(state, "itm_potion_xp")

        assertTrue(outcome.state.inventory.isEmpty())
        assertEquals(2, outcome.state.player.level)
        assertTrue(outcome.events.any { it is GameEvent.LevelUp })
    }

    @Test
    fun `using an item not in inventory fails`() {
        val outcome = GameEngine.useItem(newGame, "itm_potion_xp")

        assertSame(newGame, outcome.state)
        assertFalse(outcome.events.isEmpty())
    }
}
