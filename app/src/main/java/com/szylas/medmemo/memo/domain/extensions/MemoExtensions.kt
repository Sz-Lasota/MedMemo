package com.szylas.medmemo.memo.domain.extensions

import com.szylas.medmemo.common.domain.models.Memo
import com.szylas.medmemo.common.domain.models.MemoNotification
import java.util.Calendar

fun Memo.provideTimes(): List<Int> {
    dosageTime = MutableList(this.numberOfDoses) {
        it * this.gap + 8 * 60
    }
    return dosageTime
}

fun Memo.id(): String {
    return "${name}_${startDate.timeInMillis}"
}

fun Memo.generateNotifications() {
    val idBase = (Calendar.getInstance().timeInMillis % 100_000).toInt()
    var index = 0
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
                    index++
                    continue
                }
                notifications.add(
                    MemoNotification(
                        date = notifiyDate,
                        name = name,
                        notificationId = idBase + index
                    )
                )
                date.add(Calendar.DATE, 1)
                index++
            }
            notifications.add(MemoNotification(date = Calendar.getInstance().apply {
                timeInMillis = date.timeInMillis
                set(Calendar.HOUR_OF_DAY, hour / 60)
                set(Calendar.MINUTE, hour % 60)
            }, name = name, notificationId = idBase + index))
            index++
        } else {
            for (i in 1..14) {
                val notifiyDate = Calendar.getInstance().apply {
                    timeInMillis = date.timeInMillis
                    set(Calendar.HOUR_OF_DAY, hour / 60)
                    set(Calendar.MINUTE, hour % 60)
                }
                if (notifiyDate.before(now)) {
                    date.add(Calendar.DATE, 1)
                    index++
                    continue
                }
                notifications.add(
                    MemoNotification(
                        date = notifiyDate,
                        name = name,
                        notificationId = idBase + index
                    )
                )
                date.add(Calendar.DATE, 1)
                index++
            }
        }
    }
}

