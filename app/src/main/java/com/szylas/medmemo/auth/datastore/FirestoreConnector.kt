package com.szylas.medmemo.auth.datastore

import com.google.firebase.firestore.FirebaseFirestore

class FirestoreConnector {

    private data class User(val name: String = "")

    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
    private val firestoreCollection = "users"

    fun addUser(eMail: String, name: String, onSuccess: (String) -> Unit, onError: (String) -> Unit) {
        firestore.collection(firestoreCollection)
            .document(eMail)
            .set(User(name = name))
            .addOnSuccessListener { onSuccess("Successfully created account for: $eMail") }
            .addOnFailureListener { onError(it.message ?: "Unknown error when creating user!")}
    }

    fun getUser(eMail: String, onSuccess: (String) -> Unit, onError: (String) -> Unit) {
        firestore.collection(firestoreCollection)
            .document(eMail)
            .get()
            .addOnSuccessListener {
                onSuccess(it.toObject(User::class.java)?.name ?: "Unknown")
            }
            .addOnFailureListener {
                onError(it.message ?: "Unknown error when logging in!")
            }
    }
}