package com.szylas.medmemo.auth.domain.validation

fun validateEmail(email: String): Boolean {
    return Regex("^[\\w-.]+@([\\w-]+\\.)+[\\w-]{2,4}\$", RegexOption.IGNORE_CASE).matches(email)
}

fun validatePassword(password: String): Boolean {
    return Regex("^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}\$").matches(password)
}

fun validateRepeatedPassword(password: String, repeatedPassword: String): Boolean {
    return password == repeatedPassword
}