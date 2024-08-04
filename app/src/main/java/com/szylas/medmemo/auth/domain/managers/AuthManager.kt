package com.szylas.medmemo.auth.domain.managers

import com.szylas.medmemo.auth.datastore.IAuthConnector
import com.szylas.medmemo.auth.domain.models.LoginCredentials
import com.szylas.medmemo.auth.domain.models.RegisterCredentials
import com.szylas.medmemo.auth.domain.validation.validateEmail
import com.szylas.medmemo.auth.domain.validation.validatePassword
import com.szylas.medmemo.auth.domain.validation.validateRepeatedPassword
import kotlinx.coroutines.Dispatchers

class AuthManager(
    private val connector: IAuthConnector
) {

    suspend fun login(
        credentials: LoginCredentials,
        onSuccess: (String) -> Unit,
        onError: (String) -> Unit
    ) = with(Dispatchers.IO) {
        if (!validateEmail(credentials.eMail)) {
            onError("Invalid email!")
            return@with
        }

        connector.login(credentials, onSuccess, onError)
    }

    suspend fun register(
        credentials: RegisterCredentials,
        onSuccess: (String) -> Unit,
        onError: (String) -> Unit
    ) = with(Dispatchers.IO) {
        if (credentials.name.isBlank()) {
            onError("Name field cannot be empty!")
            return@with
        }
        if (!validateEmail(credentials.eMail)) {
            onError("Invalid email!")
            return@with
        }
        if (!validatePassword(credentials.password)) {
            onError("Password should contain at least 8 characters, minimum one letter and one number!")
            return@with
        }
        if (!validateRepeatedPassword(credentials.password, credentials.repeatedPassword)) {
            onError("Password and repeated password do not match!")
            return@with
        }

        connector.register(credentials, onSuccess, onError)
    }

}