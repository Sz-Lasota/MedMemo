package com.szylas.medmemo.auth.domain

import com.szylas.medmemo.auth.datastore.session.FirebaseSessionStore

object Session {
    // TODO: Prepare for other means of authentication
    private val defaultStore = FirebaseSessionStore()

    fun user(): String? {
        return defaultStore.getUsername()
    }

    fun email(): String? {
        return defaultStore.getEmail()
    }

    fun signOut() {
        defaultStore.signOut()
    }
}