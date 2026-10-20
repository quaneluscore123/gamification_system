package com.lifequest.app.domain.player

import kotlinx.serialization.Serializable

@Serializable
data class PlayerProgress(
    val level: Int = 1,
    val xp: Int = 0,
    val gold: Int = 0,
    val title: String = "Beginner",
    val legacy: List<String> = emptyList(),
    val streak: Int = 0,
)

data class RewardResult(
    val progress: PlayerProgress,
    val levelUp: Boolean,
)
