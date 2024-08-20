package com.szylas.medmemo.memo.datastore

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
import com.szylas.medmemo.common.domain.models.Memo
import com.szylas.medmemo.memo.datastore.mappers.fromMemo
import com.szylas.medmemo.memo.datastore.mappers.toMemo
import com.szylas.medmemo.memo.datastore.models.MemoEntity
import com.szylas.medmemo.memo.domain.extensions.id
import kotlinx.coroutines.tasks.await
import java.util.Calendar

class FirebaseMemoRepository : IMemoRepository {

    private val firebase = FirebaseFirestore.getInstance()

    override suspend fun saveMemo(
        memo: Memo,
        user: String,
        onSuccess: (String) -> Unit,
        onError: (String) -> Unit
    ) {
        firebase.collection(user)
            .document(memo.id())
            .set(fromMemo(memo))
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
        firebase.collection(user)
            .document(memo.id())
            .delete()
            .addOnSuccessListener {
                onSuccess("Successfully deleted: ${memo.name}")
            }.addOnFailureListener {
                onError(it.message ?: "Unknown error when updating memo")
            }
    }

    override suspend fun updateMemo(
        memo: Memo,
        user: String,
        onSuccess: (String) -> Unit,
        onError: (String) -> Unit
    ) {
        firebase.collection(user)
            .document(memo.id())
            .set(fromMemo(memo))
            .addOnSuccessListener {
                onSuccess("Successfully updated memo: ${memo.name}!")
            }.addOnFailureListener {
                onError(it.message ?: "Unknown error when updating memo")
            }
    }

    override suspend fun loadActive(
        user: String,
        onSuccess: (List<Memo>) -> Unit,
        onError: (String) -> Unit
    ) {
        val today = Calendar.getInstance()
        firebase.collection(user)
            .get()
            .addOnSuccessListener { documents ->
                onSuccess(
                    documents.map { it.toObject(MemoEntity::class.java) }
                        .map { toMemo(it) }
                        .filter { it.finishDate == null || !it.finishDate!!.before(today) }
                        .toList()
                )
            }
            .addOnFailureListener {
                onError(it.message ?: "Unknown error when getting memo")
            }
    }

    override suspend fun loadActiveAndEndless(user: String, onError: (String) -> Unit): List<Memo> {
        val data = firebase.collection(user)
            .get().addOnFailureListener {
                onError(it.message ?: "Unknown error when getting memo")
            }.await()

        return data.map { it.toObject(MemoEntity::class.java) }
            .map { toMemo(it) }
            .filter { it.finishDate == null }
            .toList()
    }


}