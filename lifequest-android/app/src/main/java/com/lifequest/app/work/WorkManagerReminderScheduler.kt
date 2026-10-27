package com.lifequest.app.work

import android.content.Context
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.lifequest.app.domain.settings.ReminderScheduler
import dagger.hilt.android.qualifiers.ApplicationContext
import java.time.Clock
import java.time.Duration
import java.time.LocalDateTime
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class WorkManagerReminderScheduler @Inject constructor(
    @ApplicationContext private val context: Context,
    private val clock: Clock,
) : ReminderScheduler {

    override fun schedule(hour: Int) {
        val request = PeriodicWorkRequestBuilder<ReminderWorker>(1, TimeUnit.DAYS)
            .setInitialDelay(delayUntil(hour).toMillis(), TimeUnit.MILLISECONDS)
            .build()

        // "Unique work": chỉ tồn tại một lịch nhắc. Đổi giờ thì hủy lịch cũ, tạo lịch mới.
        WorkManager.getInstance(context)
            .enqueueUniquePeriodicWork(WORK_NAME, ExistingPeriodicWorkPolicy.CANCEL_AND_REENQUEUE, request)
    }

    override fun cancel() {
        WorkManager.getInstance(context).cancelUniqueWork(WORK_NAME)
    }

    /** Khoảng thời gian từ bây giờ tới hour:00 kế tiếp (hôm nay, hoặc ngày mai nếu đã qua). */
    private fun delayUntil(hour: Int): Duration {
        val now = LocalDateTime.now(clock)
        val todayAtHour = now.toLocalDate().atTime(hour, 0)
        val next = if (todayAtHour.isAfter(now)) todayAtHour else todayAtHour.plusDays(1)
        return Duration.between(now, next)
    }

    private companion object {
        const val WORK_NAME = "daily_quest_reminder"
    }
}
