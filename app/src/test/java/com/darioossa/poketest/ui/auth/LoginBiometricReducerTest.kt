package com.darioossa.poketest.ui.auth

import com.darioossa.poketest.domain.model.AuthMethod
import com.darioossa.poketest.domain.model.AuthResult
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class LoginBiometricReducerTest {
    private val reducer = LoginReducer()

    @Test
    fun biometricClickSetsSubmitting() {
        val state = LoginState()

        val (newState, effect) = reducer.reduce(state, LoginEvent.BiometricClicked)

        assertTrue(newState.isSubmitting)
        assertNull(effect)
    }

    @Test
    fun biometricErrorSetsError() {
        val state = LoginState(isSubmitting = true)
        val result = AuthResult.Error(
            method = AuthMethod.BIOMETRIC,
            code = "BIOMETRIC_FAILED",
            message = "Biometric failed"
        )

        val (newState, effect) = reducer.reduce(state, LoginEvent.AuthResultReceived(result))

        assertEquals(LoginError.BiometricFailed, newState.error)
        assertNull(effect)
    }

    @Test
    fun biometricUnavailableSetsNotReadyError() {
        val state = LoginState(isSubmitting = true)
        val result = AuthResult.Error(
            method = AuthMethod.BIOMETRIC,
            code = "BIOMETRIC_UNAVAILABLE",
            message = "unavailable"
        )

        val (newState, effect) = reducer.reduce(state, LoginEvent.AuthResultReceived(result))

        assertEquals(LoginError.BiometricNotReady, newState.error)
        assertNull(effect)
    }

    @Test
    fun biometricCanceledSetsCanceledError() {
        val state = LoginState(isSubmitting = true)
        val result = AuthResult.Canceled(
            method = AuthMethod.BIOMETRIC,
            message = "canceled"
        )

        val (newState, effect) = reducer.reduce(state, LoginEvent.AuthResultReceived(result))

        assertTrue(newState.error is LoginError.Canceled)
        assertNull(effect)
    }
}
