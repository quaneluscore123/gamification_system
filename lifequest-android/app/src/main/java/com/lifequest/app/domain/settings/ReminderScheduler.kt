package com.lifequest.app.domain.settings

/** Port lên lịch nhắc nhở. Bản cài đặt dùng WorkManager; trong test có thể thay bằng bản giả. */
interface ReminderScheduler {
    fun schedule(hour: Int)
    fun cancel()
}
