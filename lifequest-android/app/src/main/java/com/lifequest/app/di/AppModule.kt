package com.lifequest.app.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import java.time.Clock

/** Dependency không phải class của mình (Clock của JDK) thì dùng @Provides thay vì @Inject constructor. */
@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    /** Inject Clock thay vì gọi LocalDate.now() trực tiếp, để test có thể "tua" thời gian. */
    @Provides
    fun provideClock(): Clock = Clock.systemDefaultZone()
}
