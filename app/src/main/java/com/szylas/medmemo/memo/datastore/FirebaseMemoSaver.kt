package com.szylas.medmemo.memo.datastore

import com.google.firebase.firestore.FirebaseFirestore
import com.szylas.medmemo.common.domain.models.Memo
import com.szylas.medmemo.memo.domain.extensions.id

class FirebaseMemoSaver: IMemoSaver {

    private val firebase = FirebaseFirestore.getInstance()

    override suspend fun saveMemo(
        memo: Memo,
        user: String,
        onSuccess: (String) -> Unit,
        onError: (String) -> Unit
    ) {
        firebase.collection(user)
            .document(memo.id())
            .set(memo)
            .addOnSuccessListener {
                onSuccess("Successfully created memo: ${memo.name}!")
            }.addOnFailureListener {
                onError(it.message ?: "Unknown error when persisting memo")
            }
    }

    override suspend fun deleteMemo(
        memo: Memo,
        user: String,
        onSuccess: (String) -> Unit,
        onError: (String) -> Unit
    ) {
        TODO("Not yet implemented")
    }

    override suspend fun updateMemo(
        memo: Memo,
        user: String,
        onSuccess: (String) -> Unit,
        onError: (String) -> Unit
    ) {
        firebase.collection(user)
            .document(memo.id())
            .set(memo)
            .addOnSuccessListener {
                onSuccess("Successfully updated memo: ${memo.name}!")
            }.addOnFailureListener {
                onError(it.message ?: "Unknown error when updating memo")
            }
    }


}