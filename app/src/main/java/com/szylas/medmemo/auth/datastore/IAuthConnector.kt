package com.szylas.medmemo.auth.datastore

import com.szylas.medmemo.auth.domain.models.LoginCredentials
import com.szylas.medmemo.auth.domain.models.RegisterCredentials

interface IAuthConnector {

    suspend fun login(
        credentials: LoginCredentials,
        onSuccess: (String) -> Unit,
        onError: (String) -> Unit
    )

    suspend fun register(
        credentials: RegisterCredentials,
        onSuccess: (String) -> Unit,
        onError: (String) -> Unit
    )
}