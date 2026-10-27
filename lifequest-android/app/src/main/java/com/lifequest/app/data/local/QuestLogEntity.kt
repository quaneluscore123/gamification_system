package com.lifequest.app.data.local

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.lifequest.app.domain.history.QuestLog

/** Một dòng trong bảng quest_log. Index theo thời gian vì luôn sắp xếp theo cột này. */
@Entity(tableName = "quest_log", indices = [Index("completedAt")])
data class QuestLogEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val questId: String,
    val title: String,
    val xp: Int,
    val gold: Int,
    val completedAt: Long,
)

/** Kết quả của câu SELECT COUNT/SUM: Room tự map cột vào field cùng tên. */
data class TotalsRow(
    val count: Int,
    val xp: Int,
    val gold: Int,
)

// Mapper giữa tầng data và domain: domain không biết @Entity là gì
fun QuestLogEntity.toDomain() = QuestLog(questId, title, xp, gold, completedAt)

fun QuestLog.toEntity() = QuestLogEntity(
    questId = questId,
    title = title,
    xp = xp,
    gold = gold,
    completedAt = completedAtMillis,
)
