package com.szylas.medmemo

import com.szylas.medmemo.auth.domain.models.LoginCredentials
import com.szylas.medmemo.auth.domain.models.RegisterCredentials
import org.junit.Test

class ModelsTests {

    @Test
    fun loginCredentials() {
        val login = "login"
        val pass = "pass"

        val creds = LoginCredentials(login, pass)

        assert(
            creds.eMail == login && creds.password == pass
        )
    }

    @Test
    fun registerCredentials() {
        val login = "login"
        val name = "name"
        val pass = "pass"
        val repeat = "pass"

        val creds = RegisterCredentials(login, name, pass, repeat)

        assert(
            creds.eMail == login && creds.password == pass
                    && creds.repeatedPassword == repeat && creds.name == name
        )
    }
}