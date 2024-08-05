package com.szylas.medmemo.auth.datastore

import com.google.firebase.auth.FirebaseAuth
import com.szylas.medmemo.auth.domain.models.LoginCredentials
import com.szylas.medmemo.auth.domain.models.RegisterCredentials

class FirebaseAuthConnector : IAuthConnector {

    private val firestoreConnector = FirestoreConnector()

    override suspend fun login(
        credentials: LoginCredentials,
        onSuccess: (String) -> Unit,
        onError: (String) -> Unit
    ) {
        FirebaseAuth.getInstance()
            .signInWithEmailAndPassword(credentials.eMail, credentials.password)
            .addOnSuccessListener {
                firestoreConnector.getUser(credentials.eMail, onSuccess=onSuccess, onError=onError)
            }
            .addOnFailureListener {
                onError(it.message ?: "Unknown error when logging in!")
            }
    }

    override suspend fun register(
        credentials: RegisterCredentials,
        onSuccess: (String) -> Unit,
        onError: (String) -> Unit
    ) {
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(credentials.eMail, credentials.password)
            .addOnSuccessListener {
                firestoreConnector.addUser(credentials.eMail, credentials.name, onSuccess= onSuccess, onError = onError)
            }
            .addOnFailureListener {
                onError(it.message ?: "Unknown error when creating user!")
            }
    }
}