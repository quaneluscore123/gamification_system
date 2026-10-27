package com.lifequest.app.di

import android.content.Context
import androidx.room.Room
import com.lifequest.app.data.local.LifeQuestDatabase
import com.lifequest.app.data.local.QuestLogDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    /** Database phải là singleton: mở nhiều instance cùng lúc vừa tốn tài nguyên vừa dễ lỗi. */
    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): LifeQuestDatabase =
        Room.databaseBuilder(context, LifeQuestDatabase::class.java, LifeQuestDatabase.NAME).build()

    @Provides
    fun provideQuestLogDao(db: LifeQuestDatabase): QuestLogDao = db.questLogDao()
}
