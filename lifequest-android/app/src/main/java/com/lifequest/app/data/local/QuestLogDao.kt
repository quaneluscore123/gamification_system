package com.lifequest.app.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

/**
 * Room kiểm tra câu SQL ngay lúc biên dịch.
 * Hàm trả về Flow sẽ tự phát lại dữ liệu mới mỗi khi bảng thay đổi → UI luôn cập nhật.
 */
@Dao
interface QuestLogDao {

    @Insert
    suspend fun insert(entity: QuestLogEntity)

    @Query("SELECT * FROM quest_log ORDER BY completedAt DESC LIMIT :limit")
    fun observeRecent(limit: Int): Flow<List<QuestLogEntity>>

    @Query("SELECT COUNT(*) AS count, COALESCE(SUM(xp), 0) AS xp, COALESCE(SUM(gold), 0) AS gold FROM quest_log")
    fun observeTotals(): Flow<TotalsRow>

    @Query("DELETE FROM quest_log")
    suspend fun clear()
}
