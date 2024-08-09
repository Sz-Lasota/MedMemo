package com.szylas.medmemo.auth.datastore

import android.util.Log
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
                firestoreConnector.getUser(
                    credentials.eMail,
                    onSuccess = onSuccess,
                    onError = onError
                )
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
        FirebaseAuth.getInstance()
            .createUserWithEmailAndPassword(credentials.eMail, credentials.password)
            .addOnSuccessListener {
                firestoreConnector.addUser(
                    credentials.eMail,
                    credentials.name,
                    onSuccess = onSuccess,
                    onError = onError
                )
            }
            .addOnFailureListener {
                onError(it.message ?: "Unknown error when creating user!")
            }
    }

    override suspend fun checkForSession(onSuccess: (String) -> Unit) {
        val user = FirebaseAuth.getInstance().currentUser ?: return

        Log.d("USERNAME", user.email!!)

        firestoreConnector.getUser(
            eMail = user.email!!,
            onSuccess = onSuccess,
            onError = { Log.e("Restore session", "Could not restore session: $it") })

    }
}