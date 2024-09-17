package com.szylas.medmemo.statistics.datastore

import com.szylas.medmemo.common.domain.models.Memo

interface IMemoRepository {

    suspend fun fetchAll(user: String, onSuccess: (List<Memo>) -> Unit, onError: (String) -> Unit)
}