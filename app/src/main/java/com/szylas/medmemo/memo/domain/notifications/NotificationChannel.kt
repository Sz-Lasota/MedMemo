package com.szylas.medmemo.memo.domain.notifications

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import com.szylas.medmemo.memo.datastore.NotificationStore

fun registerNotificationChannel(context: Context) {
    val channel = NotificationChannel(
        NotificationStore.CHANNEL_ID,
        NotificationStore.CHANNEL_NAME,
        NotificationManager.IMPORTANCE_DEFAULT
    ).apply {
        description = NotificationStore.CHANNEL_DESCRIPTION
    }
    val notificationManager: NotificationManager =
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    notificationManager.createNotificationChannel(channel)
}