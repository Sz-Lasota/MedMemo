package com.szylas.medmemo.memo.domain.managers

import android.util.Log
import androidx.core.text.toSpannable
import com.szylas.medmemo.auth.domain.Session
import com.szylas.medmemo.common.domain.models.Memo
import com.szylas.medmemo.common.domain.models.MemoNotification
import com.szylas.medmemo.memo.datastore.IMemoRepository
import com.szylas.medmemo.memo.domain.extensions.updateEndless

class MemoManager(
    private val saver: IMemoRepository
) {

    suspend fun saveMemo(
        memo: Memo,
        onSuccess: (String) -> Unit,
        onError: (String) -> Unit,
        onSessionNotFound: () -> Unit
    ) {
        val user = Session.user()
        if (user == null) {
            onSessionNotFound()
            return
        }

        saver.saveMemo(memo = memo, user = user, onSuccess = onSuccess, onError = onError)
    }

    suspend fun deleteMemo(
        memo: Memo,
        onSuccess: (String) -> Unit,
        onError: (String) -> Unit,
        onSessionNotFound: () -> Unit
    ) {
        val user = Session.user()
        if (user == null) {
            onSessionNotFound()
            return
        }

        saver.deleteMemo(memo = memo, user = user, onSuccess = onSuccess, onError = onError)
    }

    suspend fun updateMemo(
        memo: Memo,
        notification: MemoNotification,
        onSuccess: (String) -> Unit,
        onError: (String) -> Unit,
        onSessionNotFound: (String) -> Unit
    ) {
        val user = Session.user()
        if (user == null) {
            onSessionNotFound("Session not found, log in and try again!")
            return
        }

        saver.updateNotification(memo = memo, notification = notification, user = user, onSuccess = onSuccess, onError = onError)
    }

    suspend fun loadActive(
        onSuccess: (List<Memo>) -> Unit,
        onError: (String) -> Unit,
        onSessionNotFound: () -> Unit
    ) {
        val user = Session.user()
        if (user == null) {
            onSessionNotFound()
            return
        }
        saver.loadActive(user, onSuccess, onError)
    }

    suspend fun updateEndless(
        onSuccess: (Map<Memo, List<MemoNotification>>) -> Unit,
        onError: (String) -> Unit,
        onSessionNotFound: () -> Unit
    ) {
        val user = Session.user()
        if (user == null) {
            onSessionNotFound()
            return
        }

        val memos = saver.loadActiveAndEndless(user, onError)
        val newNotifications: MutableMap<Memo, List<MemoNotification>> = mutableMapOf()
        memos.forEach { memo ->
            newNotifications[memo] = memo.updateEndless()
            if (newNotifications[memo]?.isEmpty() != true) {
                saver.updateMemo(memo, user, { Log.d("Updated memo", memo.name) }, onError)
            }
        }

        onSuccess(newNotifications)
    }


}