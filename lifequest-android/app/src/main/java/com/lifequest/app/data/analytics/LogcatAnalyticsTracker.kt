package com.lifequest.app.data.analytics

import android.util.Log
import com.lifequest.app.domain.analytics.AnalyticsTracker

/** Dùng khi chưa có google-services.json: xem sự kiện trong Logcat, lọc theo tag "Analytics". */
class LogcatAnalyticsTracker : AnalyticsTracker {

    override val backendName = "Logcat (chưa cấu hình Firebase)"

    override fun logEvent(name: String, params: Map<String, Any>) {
        Log.d(TAG, "$name $params")
    }

    override fun recordNonFatal(error: Throwable) {
        Log.w(TAG, "non-fatal", error)
    }

    private companion object {
        const val TAG = "Analytics"
    }
}
