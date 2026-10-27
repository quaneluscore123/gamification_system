package com.lifequest.app.domain.analytics

/**
 * Ghi lại hành vi người dùng (analytics) và lỗi không làm crash app.
 * Có Firebase thì gửi lên Firebase; chưa cấu hình thì chỉ ghi Logcat.
 */
interface AnalyticsTracker {
    /** Tên backend đang dùng, hiển thị trong màn hình Cài đặt. */
    val backendName: String

    fun logEvent(name: String, params: Map<String, Any> = emptyMap())

    fun recordNonFatal(error: Throwable)
}
