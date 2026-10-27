package com.lifequest.app.work

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.lifequest.app.MainActivity
import com.lifequest.app.R

object ReminderNotifications {

    private const val CHANNEL_ID = "daily_reminder"
    private const val NOTIFICATION_ID = 1001

    /** Từ Android 8 mọi notification phải thuộc một channel; người dùng tắt/bật được theo từng channel. */
    private fun ensureChannel(context: Context) {
        val channel = NotificationChannel(CHANNEL_ID, "Nhắc làm Quest", NotificationManager.IMPORTANCE_DEFAULT).apply {
            description = "Nhắc bạn làm quest Daily để giữ streak"
        }
        context.getSystemService(NotificationManager::class.java).createNotificationChannel(channel)
    }

    fun show(context: Context) {
        // Android 13+ cần người dùng cấp quyền POST_NOTIFICATIONS lúc chạy
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
            ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        ensureChannel(context)

        // PendingIntent: "giấy ủy quyền" để hệ thống mở MainActivity thay app khi người dùng chạm thông báo
        val openApp = PendingIntent.getActivity(
            context,
            0,
            Intent(context, MainActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP),
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT,
        )

        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_stat_quest)
            .setContentTitle("⚔️ Quest Daily đang chờ bạn!")
            .setContentText("Hoàn thành ít nhất một quest hôm nay để giữ chuỗi 🔥")
            .setContentIntent(openApp)
            .setAutoCancel(true)
            .build()

        NotificationManagerCompat.from(context).notify(NOTIFICATION_ID, notification)
    }
}
