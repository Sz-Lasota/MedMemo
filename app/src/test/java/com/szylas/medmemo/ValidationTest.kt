package com.szylas.medmemo

import com.szylas.medmemo.auth.domain.validation.validateEmail
import com.szylas.medmemo.auth.domain.validation.validatePassword
import com.szylas.medmemo.auth.domain.validation.validateRepeatedPassword
import junit.framework.TestCase.assertFalse
import org.junit.Test

class ValidationTest {

    @Test
    fun validateCorrectEmail() {
        val email = "correct@mail.com"

        val actual = validateEmail(email)

        assert(actual)
    }

    @Test
    fun validateEmailWithoutAt() {
        val email = "correctmail.com"

        val actual = validateEmail(email)

        assertFalse(actual)
    }

    @Test
    fun validateEmailWithoutDot() {
        val email = "correct@mailcom"

        val actual = validateEmail(email)

        assertFalse(actual)
    }

    @Test
    fun validateCorrectPassword() {
        val pass = "corrPass1"

        val actual = validatePassword(pass)

        assert(actual)
    }

    @Test
    fun validateLowercasePass() {
        val pass = "password1"

        val actual = validatePassword(pass)

        assert(actual)
    }

    @Test
    fun validateShortPass() {
        val pass = "pass"

        val actual = validatePassword(pass)

        assertFalse(actual)
    }

    @Test
    fun validateAlfaPass() {
        val pass = "passwordpass"

        val actual = validatePassword(pass)

        assertFalse(actual)
    }

    @Test
    fun validateCorrectRepeatPass() {
        val pass = "PassCorrect1"
        val repeatPass = "PassCorrect1"

        val actual = validateRepeatedPassword(pass, repeatPass)

        assert(actual)
    }

    @Test
    fun validateInvalidRepeatPass() {
        val pass = "PassCorrect1"
        val repeatPass = "PassCorrect2"

        val actual = validateRepeatedPassword(pass, repeatPass)

        assertFalse(actual)
    }

}