package com.lifequest.app.domain.quest

import kotlinx.serialization.Serializable

@Serializable
enum class QuestStatus { LOCKED, AVAILABLE, IN_PROGRESS, COMPLETED, CLAIMED }

enum class QuestType { DAILY, WEEKLY, MAIN, OPTIONAL }

enum class Difficulty { EASY, MEDIUM, HARD, BOSS }

data class QuestRewards(
    val xp: Int,
    val gold: Int,
    val bossDamage: Int,
)

data class QuestDef(
    val id: String,
    val title: String,
    val type: QuestType,
    val difficulty: Difficulty,
    val estimatedTimeMin: Int,
    val tags: List<String>,
    val rewards: QuestRewards,
)

@Serializable
data class QuestProgress(
    val questId: String,
    val status: QuestStatus,
)

val QuestStatus.isDone: Boolean
    get() = this == QuestStatus.COMPLETED || this == QuestStatus.CLAIMED
