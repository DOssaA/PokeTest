package com.darioossa.poketest.ui.auth

import com.darioossa.poketest.domain.model.AuthMethod
import com.darioossa.poketest.domain.model.AuthResult
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class LoginGoogleReducerTest {
    private val reducer = LoginReducer()

    @Test
    fun googleClickSetsSubmitting() {
        val state = LoginState()

        val (newState, effect) = reducer.reduce(state, LoginEvent.GoogleClicked)

        assertEquals(LoginScreenStep.GOOGLE, newState.screen)
        assertTrue(!newState.isSubmitting)
        assertNull(effect)
    }

    @Test
    fun googleErrorSetsError() {
        val state = LoginState(isSubmitting = true)
        val result = AuthResult.Error(
            method = AuthMethod.GOOGLE,
            code = "GOOGLE_FAILED",
            message = "Google failed"
        )

        val (newState, effect) = reducer.reduce(state, LoginEvent.AuthResultReceived(result))

        assertEquals(LoginError.GoogleFailed, newState.error)
        assertEquals(LoginScreenStep.LOGIN, newState.screen)
        assertNull(effect)
    }

    @Test
    fun googleSubmitSetsSubmitting() {
        val state = LoginState(screen = LoginScreenStep.GOOGLE)

        val (newState, effect) = reducer.reduce(state, LoginEvent.GoogleSubmit)

        assertTrue(newState.isSubmitting)
        assertNull(effect)
    }

    @Test
    fun googleCanceledReturnsToLogin() {
        val state = LoginState(screen = LoginScreenStep.GOOGLE, isSubmitting = true)
        val result = AuthResult.Canceled(
            method = AuthMethod.GOOGLE,
            message = "canceled"
        )

        val (newState, effect) = reducer.reduce(state, LoginEvent.AuthResultReceived(result))

        assertTrue(newState.error is LoginError.Canceled)
        assertEquals(LoginScreenStep.LOGIN, newState.screen)
        assertNull(effect)
    }

    @Test
    fun googleLoadingKeepsScreenAndSubmitting() {
        val state = LoginState(screen = LoginScreenStep.GOOGLE)
        val result = AuthResult.Loading(
            method = AuthMethod.GOOGLE,
            retryAfterMs = 500
        )

        val (newState, effect) = reducer.reduce(state, LoginEvent.AuthResultReceived(result))

        assertTrue(newState.isSubmitting)
        assertEquals(LoginScreenStep.GOOGLE, newState.screen)
        assertNull(effect)
    }
}
