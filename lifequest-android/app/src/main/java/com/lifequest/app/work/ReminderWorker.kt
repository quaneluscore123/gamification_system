package com.lifequest.app.work

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters

/**
 * WorkManager chạy worker này kể cả khi app đã bị tắt hoặc máy vừa khởi động lại.
 * CoroutineWorker cho phép viết doWork() dạng suspend.
 */
class ReminderWorker(
    context: Context,
    params: WorkerParameters,
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        ReminderNotifications.show(applicationContext)
        return Result.success()
    }
}
