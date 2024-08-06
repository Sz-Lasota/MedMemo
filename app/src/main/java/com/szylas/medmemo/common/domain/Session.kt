package com.szylas.medmemo.common.domain

import com.szylas.medmemo.common.datastore.FirebaseSessionStore

object Session {
    // TODO: Prepare for other means of authentication
    private val defaultStore = FirebaseSessionStore()

    fun user(): String? {
        return defaultStore.getUsername()
    }
}