package com.szylas.medmemo.memo.datastore

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.szylas.medmemo.common.datastore.mappers.toMemo
import com.szylas.medmemo.common.datastore.models.MemoEntity
import com.szylas.medmemo.common.domain.models.Memo
import com.szylas.medmemo.memo.datastore.models.PillCount
import com.szylas.medmemo.memo.domain.extensions.id
import java.util.Calendar

class PillCountFirebaseRepository: IPillCountRepository {

    private val firebase = FirebaseFirestore.getInstance()

    private fun collectionName(user: String) = "${user}_count"

    override suspend fun fetchAll(
        user: String,
        onSuccess: (List<PillCount>) -> Unit,
        onError: (String) -> Unit,
    ) {
        firebase.collection(collectionName(user))
            .get()
            .addOnSuccessListener {documents ->
                onSuccess(
                    documents
                        .map { it.toObject(PillCount::class.java) }
                )
            }
            .addOnFailureListener {
                onError(it.message ?: "Unknown exception occurred during ")
            }
    }

    override suspend fun cleanUp(user: String, onSuccess: () -> Unit, onError: (String) -> Unit) {
        Log.d("Cleanup", "3")

        firebase.collection(user)
            .get()
            .addOnSuccessListener {memos ->
                Log.d("Cleanup", "4")

                val active = memos
                    .map { it.toObject(MemoEntity::class.java) }
                    .map { toMemo(it) }
                    .filter { it.finishDate == null || !it.finishDate!!.before(Calendar.getInstance()) }
                    .map { it.id() }

                Log.d("Cleanup", "5")

                firebase.collection(collectionName(user))
                    .get()
                    .addOnSuccessListener {counts ->

                        counts
                            .map { it.toObject(PillCount::class.java) }
                            .filter { !active.contains(it.id) }
                            .forEach {
                                firebase.collection(collectionName(user))
                                    .document(it.id)
                                    .delete()
                                    .addOnFailureListener {
                                        Log.e("Cleanup", it?.message ?: "Unknown error when deleting doc")
                                    }
                            }
                        Log.d("Cleanup", "7")
                        onSuccess()
                    }
                    .addOnFailureListener {
                        onError(it.message ?: "Unknown exception occurred during ")
                    }
            }
            .addOnFailureListener {
                onError(it.message ?: "Unknown exception occurred during ")
            }
    }

    override suspend fun fetchActiveMemos(
        user: String,
        onSuccess: (List<String>) -> Unit,
        onError: (String) -> Unit,
    ) {

        firebase.collection(user)
            .get()
            .addOnSuccessListener { memos ->

                firebase.collection(collectionName(user))
                    .get()
                    .addOnSuccessListener {counts ->
                        val existing = counts
                            .map { it.toObject(PillCount::class.java) }
                            .map { it.id }

                        val active = memos
                            .map { it.toObject(MemoEntity::class.java) }
                            .map { toMemo(it) }
                            .filter { it.finishDate == null || !it.finishDate!!.before(Calendar.getInstance()) }
                            .map { it.id() }
                            .filter { !existing.contains(it) }

                        onSuccess(active)
                    }
                    .addOnFailureListener {
                        onError(it.message ?: "Unknown exception occurred during loading data")
                    }
            }
            .addOnFailureListener {
                onError(it.message ?: "Unknown exception occurred during loading data")
            }
    }

    override suspend fun update(
        user: String,
        count: PillCount,
        onSuccess: () -> Unit,
        onError: (String) -> Unit,
    ) {
        firebase.collection(collectionName(user))
            .document(count.id)
            .set(count)
            .addOnSuccessListener {
                onSuccess()
            }
            .addOnFailureListener {
                onError(it.message ?: "Unknown exception occurred during ")
            }
    }

    override suspend fun decrease(
        user: String,
        memo: Memo,
        onSuccess: (String) -> Unit,
        onError: (String) -> Unit,
        onAlarm: () -> Unit,
    ) {
        firebase.collection(collectionName(user))
            .document(memo.id())
            .get()
            .addOnSuccessListener {entity ->
                Log.d("PillAmount", "1")
                if (!entity.exists()) {
                    Log.d("PillAmount", "2")
                    return@addOnSuccessListener
                }
                val count = entity.toObject(PillCount::class.java) ?: return@addOnSuccessListener
                Log.d("PillAmount", "3")

                if (count.count != 0 && count.count / count.maxAmount.toDouble() <= 0.45) {
                    onAlarm()
                }

                count.count = if (count.count >= 1) count.count - 1 else 0

                firebase.collection(collectionName(user))
                    .document(memo.id())
                    .set(count)
                    .addOnSuccessListener {
                        onSuccess("Successfully decreased pill amount")
                    }
                    .addOnFailureListener {
                        onError(it.message ?: "Unknown exception occurred during ")
                    }

            }
            .addOnFailureListener {
                onError(it.message ?: "Unknown exception occurred during ")
            }
    }
}