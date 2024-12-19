package com.szylas.medmemo.memo.domain.notifications

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import com.szylas.medmemo.common.domain.models.Memo
import com.szylas.medmemo.common.domain.models.MemoNotification
import com.szylas.medmemo.memo.domain.managers.MemoManagerProvider
import com.szylas.medmemo.memo.domain.predictions.IPrediction
import java.util.Calendar

class NotificationsScheduler(private val context: Context) {

    private val memoManager = MemoManagerProvider.memoManager

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

    fun cancelNotification(memo: Memo, notification: MemoNotification) {
        cancelAlarm(memo, notification)
    }

    fun scheduleLowPillNotification(name: String) {
        scheduleLowPillAlarm(name)
    }

    suspend fun rescheduleNotifications(
        memo: Memo,
        lastNotification: MemoNotification,
        prediction: IPrediction,
        onSuccess: (String) -> Unit,
        onError: (String) -> Unit,
        onSessionNotFound: (String) -> Unit,
    ) {
        val pendingNotification =
            memo.notifications.filter { it.baseDosageTime == lastNotification.baseDosageTime }
                .filter { it.date.after(lastNotification.date) }
                .filter { it.notificationId != lastNotification.notificationId }.minBy { it.date }

        Log.e("Rescheduling", "Notification to reschedule: $pendingNotification")
        cancelAlarm(memo, pendingNotification)
        val previousData =
            memo.notifications.filter { it.baseDosageTime == lastNotification.baseDosageTime }
                .filter { it.intakeTime != null }
        val newDate = prediction.predict(pendingNotification, previousData)

        pendingNotification.date.apply {
            set(Calendar.HOUR_OF_DAY, newDate / 60)
            set(Calendar.MINUTE, newDate % 60)
        }

        scheduleAlarm(memo, pendingNotification)
        memoManager.updateMemo(
            memo, lastNotification, onSuccess, onError, onSessionNotFound
        )

    }

    private fun scheduleLowPillAlarm(name: String) {
        val intent =
            Intent(context.applicationContext, PillAmountNotificationReceiver::class.java).apply {
                putExtra("NAME", name)
            }

        val pendingIntent = PendingIntent.getBroadcast(
            context.applicationContext,
            0,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        val alarm = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        alarm.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            Calendar.getInstance().timeInMillis + 15 * 60 * 1000L,
            pendingIntent
        )
    }

    private fun scheduleAlarm(memo: Memo, memoNotification: MemoNotification) {
        val intent = Intent(
            context.applicationContext,
            MemoNotificationReceiver::class.java
        ).apply {
            putExtra("NOTIFICATION", memoNotification)
            putExtra("MEMO", memo)
        }

        val pendingIntent = PendingIntent.getBroadcast(
            context.applicationContext,
            memoNotification.notificationId,
            intent,
            PendingIntent.FLAG_IMMUTABLE
                    or PendingIntent.FLAG_UPDATE_CURRENT
        )

        val alarm = context.getSystemService(Context.ALARM_SERVICE)
                as AlarmManager

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

    fun scheduleNotification(memo: Memo, notification: MemoNotification) {
        Log.i("MEMO_ADD_NOTIFICATION", "Memo name: ${memo.name}, time: ${notification.date}")
        scheduleAlarm(memo, notification)
    }

    suspend fun scheduleNextNotification(
        memo: Memo,
        memoNotification: MemoNotification,
        prediction: IPrediction,
    ) {
        val updatedMemo = memoManager.fetchMemo(
            memo = memo,
        ) ?: return

        val tomorrow = Calendar.getInstance().apply { add(Calendar.DATE, 1) }
        if (updatedMemo.finishDate != null && tomorrow.after(updatedMemo.finishDate)) {
            return
        }

        tomorrow.set(Calendar.HOUR_OF_DAY, memoNotification.baseDosageTime / 60)
        tomorrow.set(Calendar.MINUTE, memoNotification.baseDosageTime % 60)

        val newNotification = MemoNotification(
            date = tomorrow,
            baseDosageTime = memoNotification.baseDosageTime,
            name = memoNotification.name,
            notificationId = memoNotification.notificationId
        )
        // TODO: Calculate new time here
        if (updatedMemo.smartMode) {
            val newTime = prediction.predict(
                newNotification,
                updatedMemo.notifications
            )
            tomorrow.set(Calendar.HOUR_OF_DAY, newTime / 60)
            tomorrow.set(Calendar.MINUTE, newTime % 60)
        }

        updatedMemo.notifications.add(newNotification)
        scheduleAlarm(updatedMemo, newNotification)

        memoManager.saveMemo(
            updatedMemo,
            onSuccess = { Log.d("PERSIST", "Successfully persisted memo") },
            onError = { Log.e("PERSIST", it) },
            onSessionNotFound = { Log.e("PERSIST", "Session not found!") }
        )
    }


}