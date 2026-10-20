package com.lifequest.app.domain.player

import com.lifequest.app.domain.game.GameBalance
import kotlin.math.floor
import kotlin.math.pow

object PlayerRules {

    fun addReward(progress: PlayerProgress, xp: Int, gold: Int): RewardResult {
        var newXp = progress.xp + xp
        var newLevel = progress.level
        var levelUp = false

        var requiredXp = requiredXpForLevel(newLevel)
        while (newXp >= requiredXp) {
            newXp -= requiredXp
            newLevel++
            levelUp = true
            requiredXp = requiredXpForLevel(newLevel)
        }

        return RewardResult(
            progress = progress.copy(
                xp = newXp,
                level = newLevel,
                title = titleForLevel(newLevel),
                gold = progress.gold + gold,
            ),
            levelUp = levelUp,
        )
    }

    fun requiredXpForLevel(level: Int): Int =
        floor(GameBalance.BASE_XP_REQUIREMENT * GameBalance.XP_MULTIPLIER_PER_LEVEL.pow(level - 1)).toInt()

    fun titleForLevel(level: Int): String = when {
        level >= 50 -> "Backend Engineer"
        level >= 30 -> "Spring Warrior"
        level >= 20 -> "Junior Backend"
        level >= 10 -> "Backend Explorer"
        level >= 5 -> "Java Apprentice"
        else -> "Beginner"
    }

    fun mentorDialogue(level: Int): String = when {
        level < 5 -> "Welcome to LifeQuest! Complete tasks to gain XP and level up."
        level < 10 -> "You're getting the hang of it! Keep exploring those Java concepts."
        level < 20 -> "Impressive progress! The Spring framework awaits you soon."
        else -> "You're a true Backend Explorer now! Today's goal: Finish API Authentication."
    }
}
