package com.szylas.medmemo.memo.domain.notifications

import android.Manifest
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationManagerCompat
import com.szylas.medmemo.memo.domain.extensions.getMemo
import com.szylas.medmemo.memo.domain.extensions.getNotification

class MemoNotificationReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {

        Log.i("NOTIFICATION", "Notify")
        val notificationProvider = NotificationProvider()

        if (ActivityCompat.checkSelfPermission(
                context, Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            Log.d("Permission", "Not granted, aborting")
            return
        }


        Log.i("NOTIFICATION", "Sending")
        registerNotificationChannel(context)

        val memo = intent.getMemo()
        val memoNotification = intent.getNotification()
        val notification = notificationProvider.provide(memo, memoNotification, context)

        val notificationManager = NotificationManagerCompat.from(context)
        notificationManager.notify(memoNotification.notificationId, notification)
    }

}

