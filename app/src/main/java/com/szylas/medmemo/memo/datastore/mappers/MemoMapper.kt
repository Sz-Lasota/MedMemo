package com.szylas.medmemo.memo.datastore.mappers

import com.szylas.medmemo.common.domain.models.Memo
import com.szylas.medmemo.common.domain.models.MemoNotification
import com.szylas.medmemo.memo.datastore.models.MemoEntity
import com.szylas.medmemo.memo.datastore.models.MemoNotificationEntity
import java.util.Calendar

fun fromMemo(memo: Memo): MemoEntity {
    return MemoEntity(
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

fun fromNotification(memoNotification: MemoNotification): MemoNotificationEntity {
    return MemoNotificationEntity(
        date = memoNotification.date.timeInMillis,
        name = memoNotification.name,
        baseDosageTime = memoNotification.baseDosageTime,
        intakeTime = memoNotification.intakeTime?.timeInMillis,
        notificationId = memoNotification.notificationId,
    )
}

fun toNotification(memoNotificationEntity: MemoNotificationEntity): MemoNotification {
    return MemoNotification(
        date = Calendar.getInstance().apply { timeInMillis = memoNotificationEntity.date },
        name = memoNotificationEntity.name,
        baseDosageTime = memoNotificationEntity.baseDosageTime,
        intakeTime = memoNotificationEntity.intakeTime?. let { intake -> Calendar.getInstance().apply { timeInMillis = intake}},
        notificationId = memoNotificationEntity.notificationId,
    )
}

fun toMemo(memoEntity: MemoEntity): Memo {
    return Memo(
        name = memoEntity.name,
        numberOfDoses = memoEntity.numberOfDoses,
        smartMode = memoEntity.smartMode,
        dosageTime = memoEntity.dosageTime,
        startDate = Calendar.getInstance().apply { timeInMillis = memoEntity.startDate },
        finishDate = memoEntity.finishDate?.let {
            Calendar.getInstance().apply { timeInMillis = it }
        },
        gap = memoEntity.gap,
        notifications = memoEntity.notifications.map { toNotification(it) }.toMutableList()
    )
}