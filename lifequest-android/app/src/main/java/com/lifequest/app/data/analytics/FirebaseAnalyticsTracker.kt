package com.lifequest.app.data.analytics

import android.content.Context
import android.os.Bundle
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.lifequest.app.domain.analytics.AnalyticsTracker

/** Xem sự kiện realtime ở Firebase Console → Analytics → DebugView (xem docs/FIREBASE_SETUP.md). */
class FirebaseAnalyticsTracker(context: Context) : AnalyticsTracker {

    private val analytics = FirebaseAnalytics.getInstance(context)
    private val crashlytics = FirebaseCrashlytics.getInstance()

    override val backendName = "Firebase"

    override fun logEvent(name: String, params: Map<String, Any>) {
        val bundle = Bundle().apply {
            params.forEach { (key, value) ->
                when (value) {
                    is Int -> putLong(key, value.toLong())
                    is Long -> putLong(key, value)
                    is Double -> putDouble(key, value)
                    else -> putString(key, value.toString())
                }
            }
        }
        analytics.logEvent(name, bundle)
        crashlytics.log("event: $name $params") // hiện trong "Logs" của mỗi crash report
    }

    override fun recordNonFatal(error: Throwable) {
        crashlytics.recordException(error)
    }
}
