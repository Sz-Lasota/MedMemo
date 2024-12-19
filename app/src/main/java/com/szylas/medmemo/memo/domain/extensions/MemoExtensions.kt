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


fun Memo.generateNotifications() {
    // TODO: Rework id provider
    val idBase = (Calendar.getInstance().timeInMillis % 1_000_000).toInt()
    var idOffset = 0

    dosageTime.forEach { hour ->
        // Set date to first day of therapy
        val startDate = Calendar.getInstance().apply { timeInMillis = startDate.timeInMillis }
        val now = Calendar.getInstance()

        val notifyDate = Calendar.getInstance().apply {
            timeInMillis = startDate.timeInMillis
            set(Calendar.HOUR_OF_DAY, hour / 60)
            set(Calendar.MINUTE, hour % 60)
        }

        // if notify date is before now, set notification to following day
        if (notifyDate.before(now)) {
            notifyDate.add(Calendar.DATE, 1)
        }

        notifications.add(
            MemoNotification(
                date = notifyDate,
                baseDosageTime = hour,
                name = name,
                notificationId = idBase + idOffset
            )
        )

        idOffset++
    }
}

