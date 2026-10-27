package com.lifequest.app.data

import com.lifequest.app.data.local.QuestLogDao
import com.lifequest.app.data.local.toDomain
import com.lifequest.app.data.local.toEntity
import com.lifequest.app.domain.history.HistoryRepository
import com.lifequest.app.domain.history.HistoryTotals
import com.lifequest.app.domain.history.QuestLog
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class RoomHistoryRepository @Inject constructor(
    private val dao: QuestLogDao,
) : HistoryRepository {

    override fun observeRecent(limit: Int): Flow<List<QuestLog>> =
        dao.observeRecent(limit).map { rows -> rows.map { it.toDomain() } }

    override fun observeTotals(): Flow<HistoryTotals> =
        dao.observeTotals().map { HistoryTotals(it.count, it.xp, it.gold) }

    override suspend fun record(log: QuestLog) = dao.insert(log.toEntity())

    override suspend fun clear() = dao.clear()
}
