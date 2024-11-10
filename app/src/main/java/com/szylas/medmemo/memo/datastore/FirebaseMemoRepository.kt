package com.szylas.medmemo.memo.datastore

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.szylas.medmemo.common.domain.models.Memo
import com.szylas.medmemo.common.datastore.mappers.fromMemo
import com.szylas.medmemo.common.datastore.mappers.toMemo
import com.szylas.medmemo.common.datastore.models.MemoEntity
import com.szylas.medmemo.common.domain.models.MemoNotification
import com.szylas.medmemo.memo.domain.extensions.id
import kotlinx.coroutines.tasks.await
import java.util.Calendar

class FirebaseMemoRepository : IMemoRepository {

    private val firebase = FirebaseFirestore.getInstance()

    override suspend fun saveMemo(
        memo: Memo,
        user: String,
        onSuccess: (String) -> Unit,
        onError: (String) -> Unit,
    ) {
        firebase.collection(user)
            .document(memo.id())
            .set(fromMemo(memo))
            .addOnSuccessListener {
                onSuccess("Successfully created memo: ${memo.name}!")
            }
            .addOnFailureListener {
                onError(it.message ?: "Unknown error when persisting memo")
            }
    }

    override suspend fun deleteMemo(
        memo: Memo,
        user: String,
        onSuccess: (String) -> Unit,
        onError: (String) -> Unit,
    ) {
        firebase.collection(user)
            .document(memo.id())
            .delete()
            .addOnSuccessListener {
                onSuccess("Successfully deleted: ${memo.name}")
            }
            .addOnFailureListener {
                onError(it.message ?: "Unknown error when updating memo")
            }
    }

    override suspend fun updateMemo(
        memo: Memo,
        user: String,
        onSuccess: (String) -> Unit,
        onError: (String) -> Unit,
    ) {
        firebase.collection(user)
            .document(memo.id())
            .set(fromMemo(memo))
            .addOnSuccessListener {
                onSuccess("Successfully updated memo: ${memo.name}!")
            }
            .addOnFailureListener {
                onError(it.message ?: "Unknown error when updating memo")
            }
    }

    override suspend fun updateNotification(
        memo: Memo,
        notification: MemoNotification,
        user: String,
        onSuccess: (String) -> Unit,
        onError: (String) -> Unit,
    ) {
        firebase.collection(user)
            .document(memo.id())
            .get()
            .addOnSuccessListener {
                val entity = it.toObject(MemoEntity::class.java)

                if (entity == null) {
                    onError("Unknown error")
                    return@addOnSuccessListener
                }
                val obj = toMemo(entity)

                val index = obj.notifications.indexOf(obj.notifications
                    .filter{ item -> item.notificationId == notification.notificationId}
                    .firstOrNull { item ->
                        item.date.get(Calendar.DAY_OF_YEAR) == notification.date.get(Calendar.DAY_OF_YEAR)
                                && item.date.get(Calendar.YEAR) == notification.date.get(Calendar.YEAR)
                    }
                )

                obj.notifications[index].intakeTime = notification.intakeTime
                Log.d("Updating", obj.notifications[index].toString())
                firebase.collection(user).document(memo.id()).set(fromMemo(obj))
                    .addOnSuccessListener {
                        onSuccess("Successfully updated memo: ${memo.name}!")
                    }
                    .addOnFailureListener {exc ->
                        onError(exc.message ?: "Unknown error when updating memo")
                    }

            }
            .addOnFailureListener {
                onError(it.message ?: "Unknown error when updating memo")
            }
    }

    override suspend fun loadActive(
        user: String,
        onSuccess: (List<Memo>) -> Unit,
        onError: (String) -> Unit,
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

    override suspend fun fetchMemo(
        user: String,
        memo: Memo,
    ): Memo? {
        val entity = firebase.collection(user)
            .document(memo.id())
            .get().await().toObject(MemoEntity::class.java)

        return entity?.let { toMemo(it) }
    }


}