package com.szylas.medmemo.memo.domain.managers

import com.szylas.medmemo.auth.domain.Session
import com.szylas.medmemo.common.domain.models.Memo
import com.szylas.medmemo.common.domain.models.MemoNotification
import com.szylas.medmemo.memo.datastore.IMemoRepository

class MemoManager(
    private val saver: IMemoRepository,
) {

    suspend fun saveMemo(
        memo: Memo,
        onSuccess: (String) -> Unit,
        onError: (String) -> Unit,
        onSessionNotFound: () -> Unit,
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
        onSessionNotFound: () -> Unit,
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
        onSessionNotFound: (String) -> Unit,
    ) {
        val user = Session.user()
        if (user == null) {
            onSessionNotFound("Session not found, log in and try again!")
            return
        }

        saver.updateNotification(
            memo = memo,
            notification = notification,
            user = user,
            onSuccess = onSuccess,
            onError = onError
        )
    }

    suspend fun loadActive(
        onSuccess: (List<Memo>) -> Unit,
        onError: (String) -> Unit,
        onSessionNotFound: () -> Unit,
    ) {
        val user = Session.user()
        if (user == null) {
            onSessionNotFound()
            return
        }
        saver.loadActive(user, onSuccess, onError)
    }



    suspend fun fetchMemo(
        memo: Memo,
    ): Memo? {
        val user = Session.user() ?: return null
        return saver.fetchMemo(user, memo)
    }


}