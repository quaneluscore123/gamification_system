package com.lifequest.app.domain.game

/**
 * Cổng (port) lưu trữ tiến trình game. Interface nằm ở domain, bản cài đặt nằm ở data,
 * nên domain không phụ thuộc DataStore hay Room (Dependency Rule của Clean Architecture).
 */
interface GameRepository {
    suspend fun load(): GameState
    suspend fun save(state: GameState)
    suspend fun clear()
}
