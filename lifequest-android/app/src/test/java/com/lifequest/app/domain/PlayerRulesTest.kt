package com.lifequest.app.domain

import com.lifequest.app.domain.player.PlayerProgress
import com.lifequest.app.domain.player.PlayerRules
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class PlayerRulesTest {

    @Test
    fun `required xp grows by 1_5x per level`() {
        assertEquals(100, PlayerRules.requiredXpForLevel(1))
        assertEquals(150, PlayerRules.requiredXpForLevel(2))
        assertEquals(225, PlayerRules.requiredXpForLevel(3))
    }

    @Test
    fun `reward below threshold does not level up`() {
        val result = PlayerRules.addReward(PlayerProgress(), xp = 50, gold = 10)

        assertFalse(result.levelUp)
        assertEquals(1, result.progress.level)
        assertEquals(50, result.progress.xp)
        assertEquals(10, result.progress.gold)
    }

    @Test
    fun `large reward can level up multiple times and carries leftover xp`() {
        // 100 (lv1) + 150 (lv2) = 250 -> lv3 với 10 XP dư
        val result = PlayerRules.addReward(PlayerProgress(), xp = 260, gold = 0)

        assertTrue(result.levelUp)
        assertEquals(3, result.progress.level)
        assertEquals(10, result.progress.xp)
    }

    @Test
    fun `title follows level thresholds`() {
        assertEquals("Beginner", PlayerRules.titleForLevel(4))
        assertEquals("Java Apprentice", PlayerRules.titleForLevel(5))
        assertEquals("Backend Explorer", PlayerRules.titleForLevel(10))
        assertEquals("Backend Engineer", PlayerRules.titleForLevel(50))
    }
}
