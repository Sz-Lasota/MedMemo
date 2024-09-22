package com.szylas.medmemo.memo.datastore

import com.szylas.medmemo.common.domain.models.Memo
import com.szylas.medmemo.memo.datastore.models.PillCount

interface IPillCountRepository {

    suspend fun fetchAll(
        user: String,
        onSuccess: (List<PillCount>) -> Unit,
        onError: (String) -> Unit,
    )

    suspend fun cleanUp(user: String, onSuccess: () -> Unit, onError: (String) -> Unit)

    suspend fun fetchActiveMemos(
        user: String,
        onSuccess: (List<String>) -> Unit,
        onError: (String) -> Unit,
    )

    suspend fun update(
        user: String,
        count: PillCount,
        onSuccess: () -> Unit,
        onError: (String) -> Unit,
    )

    suspend fun decrease(
        user: String,
        memo: Memo,
        onSuccess: (String) -> Unit,
        onError: (String) -> Unit,
        onAlarm: () -> Unit,
    )
}