package com.szylas.medmemo.memo.datastore.mappers

import com.szylas.medmemo.common.domain.models.Memo
import com.szylas.medmemo.common.domain.models.MemoNotification
import java.io.Serializable
import java.util.Calendar


data class MemoFirestore(
    var name: String = "",
    var numberOfDoses: Int = 0,
    var smartMode: Boolean = false,
    var dosageTime: List<Int> = listOf(),
    var startDate: Long = Calendar.getInstance().timeInMillis,
    var finishDate: Long? = null,
    var gap: Int = 0,
    var notifications: MutableList<MemoNotificationFirestore> = mutableListOf()
) : Serializable

data class MemoNotificationFirestore(
    val date: Long = Calendar.getInstance().timeInMillis,
    val name: String = "",
    var intakeTime: Calendar? = null,
    val notificationId: Int = 0
) : Serializable

fun fromMemo(memo: Memo): MemoFirestore {
    return MemoFirestore(
        name = memo.name,
        numberOfDoses = memo.numberOfDoses,
        smartMode = memo.smartMode,
        dosageTime = memo.dosageTime,
        startDate = memo.startDate.timeInMillis,
        finishDate = memo.finishDate?.timeInMillis,
        gap = memo.gap,
        notifications = memo.notifications.map { fromNotification(it) }.toMutableList(),
    )

}

fun fromNotification(memoNotification: MemoNotification): MemoNotificationFirestore {
    return MemoNotificationFirestore(
        date = memoNotification.date.timeInMillis,
        name = memoNotification.name,
        intakeTime = memoNotification.intakeTime,
        notificationId = memoNotification.notificationId,
    )
}

fun toNotification(memoNotificationFirestore: MemoNotificationFirestore): MemoNotification {
    return MemoNotification(
        date = Calendar.getInstance().apply { timeInMillis = memoNotificationFirestore.date },
        name = memoNotificationFirestore.name,
        intakeTime = memoNotificationFirestore.intakeTime,
        notificationId = memoNotificationFirestore.notificationId,
    )
}

fun toMemo(memoFirestore: MemoFirestore): Memo {
    return Memo(
        name = memoFirestore.name,
        numberOfDoses = memoFirestore.numberOfDoses,
        smartMode = memoFirestore.smartMode,
        dosageTime = memoFirestore.dosageTime,
        startDate = Calendar.getInstance().apply { timeInMillis = memoFirestore.startDate },
        finishDate = memoFirestore.finishDate?.let {
            Calendar.getInstance().apply { timeInMillis = it }
        },
        gap = memoFirestore.gap,
        notifications = memoFirestore.notifications.map { toNotification(it) }.toMutableList()
    )
}