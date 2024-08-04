package com.szylas.medmemo.auth.domain.models

data class LoginCredentials(
    val eMail: String,
    val password: String
)

data class RegisterCredentials(
    val eMail: String,
    val name: String,
    val password: String,
    val repeatedPassword: String
)