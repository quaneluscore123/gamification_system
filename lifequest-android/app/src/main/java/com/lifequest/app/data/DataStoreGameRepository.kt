package com.lifequest.app.data

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.lifequest.app.domain.game.GameEngine
import com.lifequest.app.domain.game.GameRepository
import com.lifequest.app.domain.game.GameState
import kotlinx.coroutines.flow.first
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json

private val Context.dataStore by preferencesDataStore(name = "lifequest")

/** Lưu toàn bộ GameState dưới dạng JSON trong DataStore (thay cho LocalStorage). */
class DataStoreGameRepository(private val context: Context) : GameRepository {

    private val json = Json { ignoreUnknownKeys = true }

    override suspend fun load(): GameState {
        val raw = context.dataStore.data.first()[KEY_STATE] ?: return GameEngine.newGame()
        return try {
            json.decodeFromString<GameState>(raw)
        } catch (e: SerializationException) {
            // Save hỏng hoặc khác phiên bản: bắt đầu lại thay vì crash app
            GameEngine.newGame()
        }
    }

    override suspend fun save(state: GameState) {
        context.dataStore.edit { it[KEY_STATE] = json.encodeToString(state) }
    }

    override suspend fun clear() {
        context.dataStore.edit { it.remove(KEY_STATE) }
    }

    private companion object {
        val KEY_STATE = stringPreferencesKey("game_state_v1")
    }
}
