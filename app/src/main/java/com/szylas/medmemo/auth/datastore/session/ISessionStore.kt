package com.szylas.medmemo.auth.datastore.session

interface ISessionStore {

    fun getUsername(): String?

    fun getEmail(): String?

    fun signOut()
}