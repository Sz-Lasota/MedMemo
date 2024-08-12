package com.szylas.medmemo.memo.datastore

import com.szylas.medmemo.common.domain.models.Memo

interface IMemoRepository {

    suspend fun saveMemo(memo: Memo, user: String, onSuccess: (String) -> Unit, onError: (String) -> Unit)

    suspend fun deleteMemo(memo: Memo, user: String, onSuccess: (String) -> Unit, onError: (String) -> Unit)

    suspend fun updateMemo(memo: Memo, user: String, onSuccess: (String) -> Unit, onError: (String) -> Unit)

    suspend fun loadActive(user: String, onSuccess: (List<Memo>) -> Unit, onError: (String) -> Unit)

}