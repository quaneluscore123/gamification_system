package com.lifequest.app.di

import com.lifequest.app.data.CachedQuoteRepository
import com.lifequest.app.data.DataStoreGameRepository
import com.lifequest.app.data.RoomHistoryRepository
import com.lifequest.app.domain.game.GameRepository
import com.lifequest.app.domain.history.HistoryRepository
import com.lifequest.app.domain.quote.QuoteRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Dạy Hilt: "ai cần GameRepository thì đưa DataStoreGameRepository".
 * Dùng @Binds vì chỉ cần nối interface với class đã có @Inject constructor.
 */
@Module
@InstallIn(SingletonComponent::class)
abstract class DataModule {

    @Binds
    @Singleton
    abstract fun bindGameRepository(impl: DataStoreGameRepository): GameRepository

    @Binds
    @Singleton
    abstract fun bindHistoryRepository(impl: RoomHistoryRepository): HistoryRepository

    @Binds
    @Singleton
    abstract fun bindQuoteRepository(impl: CachedQuoteRepository): QuoteRepository
}
