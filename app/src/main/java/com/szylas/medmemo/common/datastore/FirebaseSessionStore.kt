package com.szylas.medmemo.common.datastore

import com.google.firebase.auth.FirebaseAuth

class FirebaseSessionStore: ISessionStore {
    private val firebase = FirebaseAuth.getInstance()

    override fun getUsername(): String? {
        return firebase.currentUser?.uid
    }
}