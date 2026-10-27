package com.lifequest.app.domain.history

import kotlinx.coroutines.flow.Flow

/** Một lần hoàn thành quest, dùng cho màn hình Thống kê. */
data class QuestLog(
    val questId: String,
    val title: String,
    val xp: Int,
    val gold: Int,
    val completedAtMillis: Long,
)

data class HistoryTotals(
    val questCount: Int = 0,
    val totalXp: Int = 0,
    val totalGold: Int = 0,
)

/** Port lưu lịch sử. Bản cài đặt dùng Room nằm ở tầng data. */
interface HistoryRepository {
    fun observeRecent(limit: Int): Flow<List<QuestLog>>
    fun observeTotals(): Flow<HistoryTotals>
    suspend fun record(log: QuestLog)
    suspend fun clear()
}
