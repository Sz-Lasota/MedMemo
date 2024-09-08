package com.szylas.medmemo.memo.domain.extensions

import com.szylas.medmemo.common.domain.models.Memo
import com.szylas.medmemo.common.domain.models.MemoNotification
import java.util.Calendar

fun Memo.provideTimes(): List<Int> {
    dosageTime = MutableList(this.numberOfDoses) {
        (it * this.gap + 8 * 60) % (24 * 60)
    }.apply {
        sort()
    }
    return dosageTime
}

fun Memo.id(): String {
    return "${name}_${startDate.timeInMillis}"
}

fun Memo.updateEndless(): MutableList<MemoNotification> {
    if (finishDate != null) {
        throw RuntimeException("This is not endless memo!")
    }
    val idBase = (Calendar.getInstance().timeInMillis % 100_000).toInt() + (0..100_000).random()
    var idOffset = 0

    val stopDate = Calendar.getInstance().apply {
        add(Calendar.DATE, 7)
    }

    val notificationsMap = notifications.groupBy { it.baseDosageTime }.toMutableMap()
    val newNotifications: MutableList<MemoNotification> = mutableListOf()

    notificationsMap.forEach { (hour, memoNotifications) ->
        val newestNotificationDate = Calendar.getInstance().apply { timeInMillis = memoNotifications.maxOf { it.date }.timeInMillis }

        newestNotificationDate.set(Calendar.HOUR_OF_DAY, hour / 60)
        newestNotificationDate.set(Calendar.MINUTE, hour % 60)

        while (newestNotificationDate.before(stopDate)) {
            newestNotificationDate.add(Calendar.DATE, 1)
            val notify = MemoNotification(
                name = name,
                baseDosageTime = hour,
                date = Calendar.getInstance().apply { timeInMillis = newestNotificationDate.timeInMillis },
                notificationId = idBase + idOffset
            )
            idOffset++
            newNotifications.add(notify)
        }
    }

    notifications.addAll(newNotifications)
    return newNotifications

}


fun Memo.generateNotifications() {
    // TODO: Rework id provider
    val idBase = (Calendar.getInstance().timeInMillis % 100_000).toInt()
    var idOffset = 0
    dosageTime.forEach { hour ->
        val date = Calendar.getInstance().apply {
            timeInMillis = startDate.timeInMillis
        }

        val now = Calendar.getInstance()
        if (finishDate != null) {
            while (!date.after(finishDate)) {
                val notifiyDate = Calendar.getInstance().apply {
                    timeInMillis = date.timeInMillis
                    set(Calendar.HOUR_OF_DAY, hour / 60)
                    set(Calendar.MINUTE, hour % 60)
                }
                if (notifiyDate.before(now)) {
                    date.add(Calendar.DATE, 1)
                    idOffset++
                    continue
                }
                notifications.add(
                    MemoNotification(
                        date = notifiyDate,
                        baseDosageTime = hour,
                        name = name,
                        notificationId = idBase + idOffset
                    )
                )
                date.add(Calendar.DATE, 1)
                idOffset++
            }
            notifications.add(MemoNotification(date = Calendar.getInstance().apply {
                timeInMillis = date.timeInMillis
                set(Calendar.HOUR_OF_DAY, hour / 60)
                set(Calendar.MINUTE, hour % 60)
            }, name = name, notificationId = idBase + idOffset))
            idOffset++
        } else {
            for (i in 1..7) {
                val notifiyDate = Calendar.getInstance().apply {
                    timeInMillis = date.timeInMillis
                    set(Calendar.HOUR_OF_DAY, hour / 60)
                    set(Calendar.MINUTE, hour % 60)
                }
                if (notifiyDate.before(now)) {
                    date.add(Calendar.DATE, 1)
                    idOffset++
                    continue
                }
                notifications.add(
                    MemoNotification(
                        date = notifiyDate,
                        baseDosageTime = hour,
                        name = name,
                        notificationId = idBase + idOffset
                    )
                )
                date.add(Calendar.DATE, 1)
                idOffset++
            }
        }
    }
}

