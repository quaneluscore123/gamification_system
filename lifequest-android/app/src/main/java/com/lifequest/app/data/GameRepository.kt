package com.lifequest.app.data

import com.lifequest.app.domain.game.GameState

/** Tương đương IStorageService bên bản web. Sau này có thể thay bằng bản gọi Spring Boot API. */
interface GameRepository {
    suspend fun load(): GameState
    suspend fun save(state: GameState)
    suspend fun clear()
}
