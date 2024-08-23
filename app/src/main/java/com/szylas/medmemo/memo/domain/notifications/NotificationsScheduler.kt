package com.szylas.medmemo.memo.domain.notifications

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.szylas.medmemo.common.domain.models.Memo
import com.szylas.medmemo.common.domain.models.MemoNotification
import com.szylas.medmemo.memo.domain.managers.MemoManagerProvider
import com.szylas.medmemo.memo.domain.predictions.IPrediction
import java.util.Calendar

class NotificationsScheduler(private val context: Context) {

    val memoManager = MemoManagerProvider.memoManager

    fun scheduleNotifications(memo: Memo) {
        memo.notifications.forEach {
            scheduleAlarm(memo, it)
        }
    }

    fun cancelNotifications(memo: Memo) {
        memo.notifications.filter { it.intakeTime == null }.forEach {
            cancelAlarm(memo, it)
        }
    }

    suspend fun rescheduleNotifications(
        memo: Memo,
        lastNotification: MemoNotification,
        prediction: IPrediction,
        onSuccess: (String) -> Unit,
        onError: (String) -> Unit,
        onSessionNotFound: (String) -> Unit
    ) {
        val pendingNotification = memo.notifications
            .filter { it.baseDosageTime == lastNotification.baseDosageTime }
            .filter { it.date.after(lastNotification.date) }
            .filter { it.notificationId != lastNotification.notificationId }
            .minBy { it.date }


        cancelAlarm(memo, pendingNotification)
        val previousData = memo.notifications
            .filter { it.baseDosageTime == lastNotification.baseDosageTime }
            .filter { it.intakeTime != null }
        val newDate = prediction.predict(pendingNotification, previousData)

        pendingNotification.date.apply {
            set(Calendar.HOUR_OF_DAY, newDate / 60)
            set(Calendar.MINUTE, newDate % 60)
        }

        scheduleAlarm(memo, pendingNotification)
        memoManager.updateMemo(
            memo,
            onSuccess,
            onError,
            onSessionNotFound
        )

    }

    private fun scheduleAlarm(memo: Memo, memoNotification: MemoNotification) {
        val intent =
            Intent(context.applicationContext, MemoNotificationReceiver::class.java).apply {
                putExtra("NOTIFICATION", memoNotification)
                putExtra("MEMO", memo)
            }

        val pendingIntent = PendingIntent.getBroadcast(
            context.applicationContext,
            memoNotification.notificationId,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        val alarm = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        alarm.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            memoNotification.date.timeInMillis,
            pendingIntent
        )
    }

    private fun cancelAlarm(memo: Memo, memoNotification: MemoNotification) {
        val intent =
            Intent(context.applicationContext, MemoNotificationReceiver::class.java).apply {
                putExtra("NOTIFICATION", memoNotification)
                putExtra("MEMO", memo)
            }

        val pendingIntent = PendingIntent.getBroadcast(
            context.applicationContext,
            memoNotification.notificationId,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        alarmManager.cancel(pendingIntent)
    }


}