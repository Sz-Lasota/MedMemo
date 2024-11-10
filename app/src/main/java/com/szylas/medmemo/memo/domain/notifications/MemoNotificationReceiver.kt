package com.szylas.medmemo.memo.domain.notifications

import android.Manifest
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationManagerCompat
import com.szylas.medmemo.common.domain.models.Memo
import com.szylas.medmemo.common.domain.models.MemoNotification
import com.szylas.medmemo.memo.domain.extensions.getMemo
import com.szylas.medmemo.memo.domain.extensions.getNotification
import com.szylas.medmemo.memo.domain.predictions.WeightedAveragePrediction
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MemoNotificationReceiver : BroadcastReceiver() {

    @OptIn(DelicateCoroutinesApi::class)
    override fun onReceive(context: Context, intent: Intent) {

        Log.i("NOTIFICATION", "Notify")
        val notificationProvider = NotificationProvider()
        val notificationsScheduler = NotificationsScheduler(context)

        if (ActivityCompat.checkSelfPermission(
                context, Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            Log.d("Permission", "Not granted, aborting")
            return
        }


        Log.i("NOTIFICATION", "Sending")
        registerNotificationChannels(context)

        val memo = intent.getMemo()
        val memoNotification = intent.getNotification()
        val notification =
            notificationProvider.provideMemoNotification(memo, memoNotification, context)

        val notificationManager = NotificationManagerCompat.from(context)
        notificationManager.notify(memoNotification.notificationId, notification)

        GlobalScope.launch { scheduleNextNotification(memo, memoNotification, notificationsScheduler) }
    }

    private fun CoroutineScope.scheduleNextNotification(
        memo: Memo,
        notification: MemoNotification,
        scheduler: NotificationsScheduler
    ) = launch {
        scheduler.scheduleNextNotification(memo, notification, WeightedAveragePrediction())
    }

}

