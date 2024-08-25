package com.szylas.medmemo.main.domain

import com.szylas.medmemo.common.domain.models.Memo
import com.szylas.medmemo.common.domain.models.MemoNotification
import java.util.Calendar


// TODO: Rename file
fun getUpcomingNotifications(memos: List<Memo>): List<Pair<Memo, MemoNotification>> {
    val today = Calendar.getInstance()

    return memos.mapNotNull { memo ->
        memo.notifications
            .filter { it.date.after(today) }
            .filter { it.intakeTime == null }
            .filter { it.date.get(Calendar.DATE) == today.get(Calendar.DATE) }
            .minByOrNull { it.date }
            ?.let { memo to it }
    }.sortedBy { it.second.date }
}