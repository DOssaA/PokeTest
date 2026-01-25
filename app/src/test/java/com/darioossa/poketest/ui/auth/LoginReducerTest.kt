package com.darioossa.poketest.ui.auth

import com.darioossa.poketest.domain.model.AuthMethod
import com.darioossa.poketest.domain.model.AuthResult
import com.darioossa.poketest.domain.model.AuthToken
import com.darioossa.poketest.domain.model.CredentialStorageState
import com.darioossa.poketest.domain.model.UserCredential
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class LoginReducerTest {
    private val reducer = LoginReducer()

    @Test
    fun submitWithBlankUsernameSetsMissingUsernameError() {
        val state = LoginState(username = "", password = "secret")

        val (newState, effect) = reducer.reduce(state, LoginEvent.Submit)

        assertEquals(LoginError.MissingUsername, newState.error)
        assertEquals(false, newState.isSubmitting)
        assertNull(effect)
    }

    @Test
    fun submitWithBlankPasswordSetsMissingPasswordError() {
        val state = LoginState(username = "ash", password = "")

        val (newState, effect) = reducer.reduce(state, LoginEvent.Submit)

        assertEquals(LoginError.MissingPassword, newState.error)
        assertEquals(false, newState.isSubmitting)
        assertNull(effect)
    }

    @Test
    fun submitWithValidCredentialsSetsSubmitting() {
        val state = LoginState(username = "ash", password = "pikachu")

        val (newState, effect) = reducer.reduce(state, LoginEvent.Submit)

        assertEquals(true, newState.isSubmitting)
        assertEquals(null, newState.error)
        assertNull(effect)
    }

    @Test
    fun authSuccessNavigatesHome() {
        val state = LoginState(username = "ash", password = "pikachu", isSubmitting = true)
        val credential = UserCredential(
            credentialId = "cred-1",
            authMethod = AuthMethod.PASSWORD,
            username = "ash",
            secret = "pikachu",
            profile = null,
            lastAuthenticatedAt = 123L,
            storageState = CredentialStorageState.STORED
        )
        val result = AuthResult.Success(
            method = AuthMethod.PASSWORD,
            credential = credential,
            token = AuthToken("token")
        )

        val (newState, effect) = reducer.reduce(state, LoginEvent.AuthResultReceived(result))

        assertEquals(false, newState.isSubmitting)
        assertEquals(null, newState.error)
        assertEquals(LoginEffect.NavigateHome, effect)
    }

    @Test
    fun authErrorInvalidCredentialsSetsError() {
        val state = LoginState(username = "ash", password = "pikachu", isSubmitting = true)
        val result = AuthResult.Error(
            method = AuthMethod.PASSWORD,
            code = "INVALID_CREDENTIALS",
            message = "invalid"
        )

        val (newState, effect) = reducer.reduce(state, LoginEvent.AuthResultReceived(result))

        assertEquals(LoginError.InvalidCredentials, newState.error)
        assertEquals(false, newState.isSubmitting)
        assertNull(effect)
    }

    @Test
    fun authErrorStorageFailedSetsError() {
        val state = LoginState(username = "ash", password = "pikachu", isSubmitting = true)
        val result = AuthResult.Error(
            method = AuthMethod.PASSWORD,
            code = "STORAGE_FAILED",
            message = "failed"
        )

        val (newState, effect) = reducer.reduce(state, LoginEvent.AuthResultReceived(result))

        assertEquals(LoginError.StorageFailed, newState.error)
        assertEquals(false, newState.isSubmitting)
        assertNull(effect)
    }

    @Test
    fun clearErrorResetsError() {
        val state = LoginState(error = LoginError.MissingPassword)

        val (newState, effect) = reducer.reduce(state, LoginEvent.ClearError)

        assertNull(newState.error)
        assertNull(effect)
    }
}
