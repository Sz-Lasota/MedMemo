package com.szylas.medmemo.memo.domain.managers

import android.util.Log
import com.szylas.medmemo.auth.domain.Session
import com.szylas.medmemo.common.domain.models.Memo
import com.szylas.medmemo.memo.datastore.IPillCountRepository
import com.szylas.medmemo.memo.datastore.models.PillCount

class PillAmountManager(
    private val repository: IPillCountRepository
) {
    var counts: List<PillCount> = emptyList()
    var active: List<String> = emptyList()

    suspend fun fetchAll(onSuccess: () -> Unit, onError: (String) -> Unit) {
        val user = Session.user()
        if (user == null) {
            onError("Not logged in")
            return
        }
        repository.fetchActiveMemos(user, onSuccess = {
            active = it
        }, onError)
        repository.fetchAll(user, onSuccess = {
            counts = it
            onSuccess()
        }, onError)
    }

    suspend fun update(count: PillCount, onSuccess: () -> Unit, onError: (String) -> Unit) {
        val user = Session.user()
        if (user == null) {
            onError("Not logged in")
            return
        }

        repository.update(user, count, onSuccess, onError)
    }

    suspend fun cleanup(onSuccess: () -> Unit, onError: (String) -> Unit) {
        val user = Session.user()
        if (user == null) {
            onError("Not logged in")
            return
        }
        Log.d("Cleanup", "2")


        repository.cleanUp(user, onSuccess, onError)
    }

    suspend fun decrease(
        memo: Memo,
        onSuccess: (String) -> Unit,
        onError: (String) -> Unit,
        onAlarm: () -> Unit
    ) {
        val user = Session.user()
        if (user == null) {
            onError("Not logged in")
            return
        }

        repository.decrease(user, memo, onSuccess, onError, onAlarm)
    }
}