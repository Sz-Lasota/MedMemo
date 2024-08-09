package com.szylas.medmemo.memo.domain.notifications

import android.app.Notification
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.app.TaskStackBuilder
import com.szylas.medmemo.R
import com.szylas.medmemo.common.domain.models.Memo
import com.szylas.medmemo.common.domain.models.MemoNotification
import com.szylas.medmemo.memo.datastore.NotificationStore
import com.szylas.medmemo.memo.presentation.MemoTakenActivity

class NotificationProvider {

    fun provide(memo: Memo, memoNotification: MemoNotification, context: Context): Notification {
        val intent = Intent(context, MemoTakenActivity::class.java).apply {
            putExtra("NOTIFICATION", memoNotification)
            putExtra("MEMO", memo)
        }

        val medTakenIntent = TaskStackBuilder.create(context).run {
            addNextIntentWithParentStack(intent)
            getPendingIntent(0, PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT)
        }


        return NotificationCompat.Builder(context, NotificationStore.CHANNEL_ID)
            .setContentText(context.getString(R.string.remember_to_take_your_med))
            .setContentTitle(memoNotification.name)
            .setSmallIcon(R.drawable.ic_app_img)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setOngoing(true)
            .setAutoCancel(true)
            .setContentIntent(medTakenIntent)
            .build()
    }

}