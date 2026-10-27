package com.lifequest.app

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

/** @HiltAndroidApp sinh ra container DI gốc, sống cùng vòng đời Application. */
@HiltAndroidApp
class LifeQuestApp : Application()
