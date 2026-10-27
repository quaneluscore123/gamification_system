package com.lifequest.app.di

import android.content.Context
import com.google.firebase.FirebaseApp
import com.lifequest.app.data.analytics.FirebaseAnalyticsTracker
import com.lifequest.app.data.analytics.LogcatAnalyticsTracker
import com.lifequest.app.domain.analytics.AnalyticsTracker
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AnalyticsModule {

    /**
     * Firebase tự khởi tạo trước Application.onCreate nếu app có google-services.json.
     * Không có file đó thì FirebaseApp rỗng → dùng bản ghi Logcat để app không crash.
     */
    @Provides
    @Singleton
    fun provideAnalyticsTracker(@ApplicationContext context: Context): AnalyticsTracker =
        if (FirebaseApp.getApps(context).isNotEmpty()) FirebaseAnalyticsTracker(context) else LogcatAnalyticsTracker()
}
