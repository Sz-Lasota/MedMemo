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
    dosageTime.forEach { hour ->
        val date = Calendar.getInstance().apply {
            timeInMillis = startDate.timeInMillis
        }
        while (!date.after(finishDate)) {
            notifications.add(
                MemoNotification(
                    date = Calendar.getInstance().apply {
                        timeInMillis = date.timeInMillis
                        set(Calendar.HOUR_OF_DAY, hour / 60)
                        set(Calendar.MINUTE, hour % 60)
                    }
                )
            )
            date.add(Calendar.DATE, 1)
        }
        notifications.add(
            MemoNotification(
                date = Calendar.getInstance().apply {
                    timeInMillis = date.timeInMillis
                    set(Calendar.HOUR_OF_DAY, hour / 60)
                    set(Calendar.MINUTE, hour % 60)
                }
            )
        )
    }
}

