package com.szylas.medmemo.statistics.datastore

import com.google.firebase.firestore.FirebaseFirestore
import com.szylas.medmemo.common.datastore.mappers.toMemo
import com.szylas.medmemo.common.datastore.models.MemoEntity
import com.szylas.medmemo.common.domain.models.Memo

class FirebaseMemoRepository : IMemoRepository {

    private val firebase = FirebaseFirestore.getInstance()

    override suspend fun fetchAll(
        user: String,
        onSuccess: (List<Memo>) -> Unit,
        onError: (String) -> Unit,
    ) {
        firebase.collection(user)
            .get()
            .addOnSuccessListener { documents ->
                onSuccess(
                    documents
                        .map { it.toObject(MemoEntity::class.java) }
                        .map { toMemo(it) }
                        .toList()
                )
            }.addOnFailureListener {
                onError(it.message ?: "Unknown exception occurred during ")
            }
    }
}