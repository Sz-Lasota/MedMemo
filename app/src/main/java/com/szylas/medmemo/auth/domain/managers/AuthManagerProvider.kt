package com.szylas.medmemo.auth.domain.managers

import com.szylas.medmemo.auth.datastore.FirebaseAuthConnector

enum class AuthManagerConfiguration {
    FIREBASE
}

object AuthManagerProvider {

    private var manager: AuthManager? = null

    fun provide(config: AuthManagerConfiguration) : AuthManager {
        manager = when (config) {
            AuthManagerConfiguration.FIREBASE -> AuthManager(FirebaseAuthConnector())
        }

        return manager!!
    }

    fun use() : AuthManager? = manager

}