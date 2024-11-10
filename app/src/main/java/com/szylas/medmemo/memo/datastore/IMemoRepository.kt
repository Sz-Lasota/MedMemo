package com.szylas.medmemo.memo.datastore

import com.szylas.medmemo.common.domain.models.Memo
import com.szylas.medmemo.common.domain.models.MemoNotification

interface IMemoRepository {

    suspend fun saveMemo(
        memo: Memo,
        user: String,
        onSuccess: (String) -> Unit,
        onError: (String) -> Unit,
    )

    suspend fun deleteMemo(
        memo: Memo,
        user: String,
        onSuccess: (String) -> Unit,
        onError: (String) -> Unit,
    )

    suspend fun updateMemo(
        memo: Memo,
        user: String,
        onSuccess: (String) -> Unit,
        onError: (String) -> Unit,
    )

    suspend fun updateNotification(
        memo: Memo,
        notification: MemoNotification,
        user: String,
        onSuccess: (String) -> Unit,
        onError: (String) -> Unit,
    )

    suspend fun loadActive(user: String, onSuccess: (List<Memo>) -> Unit, onError: (String) -> Unit)

    suspend fun loadActiveAndEndless(user: String, onError: (String) -> Unit): List<Memo>
    suspend fun fetchMemo(user: String, memo: Memo): Memo?

}