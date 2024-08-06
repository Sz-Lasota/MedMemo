package com.szylas.medmemo.memo.domain.managers

import com.szylas.medmemo.common.domain.Session
import com.szylas.medmemo.common.domain.models.Memo
import com.szylas.medmemo.memo.datastore.IMemoSaver

class MemoManager(
    private val saver: IMemoSaver
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
        onSuccess: (String) -> Unit,
        onError: (String) -> Unit,
        onSessionNotFound: () -> Unit
    ) {
        val user = Session.user()
        if (user == null) {
            onSessionNotFound()
            return
        }

        saver.updateMemo(memo = memo, user = user, onSuccess = onSuccess, onError = onError)
    }


}