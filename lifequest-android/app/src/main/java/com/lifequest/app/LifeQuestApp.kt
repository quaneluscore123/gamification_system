package com.lifequest.app

import android.app.Application
import com.lifequest.app.data.DataStoreGameRepository
import com.lifequest.app.domain.game.GameRepository

/** DI thủ công: một chỗ duy nhất tạo các dependency dùng chung. */
class AppContainer(app: Application) {
    val gameRepository: GameRepository = DataStoreGameRepository(app)
}

class LifeQuestApp : Application() {
    lateinit var container: AppContainer
        private set

    override fun onCreate() {
        super.onCreate()
        container = AppContainer(this)
    }
}
