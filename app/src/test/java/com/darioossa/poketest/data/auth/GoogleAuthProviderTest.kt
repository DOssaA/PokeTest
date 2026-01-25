package com.darioossa.poketest.data.auth

import com.darioossa.poketest.data.auth.remote.GoogleAuthProvider
import com.darioossa.poketest.domain.model.AuthMethod
import com.darioossa.poketest.domain.model.AuthResult
import com.darioossa.poketest.domain.model.GoogleAuthOutcome
import com.darioossa.poketest.domain.model.GoogleAuthRequest
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class GoogleAuthProviderTest {
    @Test
    fun successReturnsTokenAndProfile() = runTest {
        val provider = GoogleAuthProvider()
        val result = provider.authenticate(GoogleAuthRequest(outcome = GoogleAuthOutcome.SUCCESS))

        assertTrue(result is AuthResult.Success)
        val success = result as AuthResult.Success
        assertEquals(AuthMethod.GOOGLE, success.method)
        assertEquals("fake-access-token", success.token?.accessToken)
        assertEquals("Poke Trainer", success.profile?.displayName)
    }

    @Test
    fun cancelReturnsCanceledResult() = runTest {
        val provider = GoogleAuthProvider()
        val result = provider.authenticate(GoogleAuthRequest(outcome = GoogleAuthOutcome.CANCELED))

        assertTrue(result is AuthResult.Canceled)
        val canceled = result as AuthResult.Canceled
        assertEquals(AuthMethod.GOOGLE, canceled.method)
    }

    @Test
    fun errorReturnsErrorResult() = runTest {
        val provider = GoogleAuthProvider()
        val result = provider.authenticate(GoogleAuthRequest(outcome = GoogleAuthOutcome.ERROR))

        assertTrue(result is AuthResult.Error)
        val error = result as AuthResult.Error
        assertEquals("GOOGLE_FAILED", error.code)
    }

    @Test
    fun loadingDelaysBeforeReturning() = runTest {
        val provider = GoogleAuthProvider()
        val result = provider.authenticate(GoogleAuthRequest(outcome = GoogleAuthOutcome.LOADING, delayMs = 500))

        assertTrue(result is AuthResult.Success)
    }

    @Test
    fun loadingWithZeroDelayReturnsSuccess() = runTest {
        val provider = GoogleAuthProvider()
        val result = provider.authenticate(GoogleAuthRequest(outcome = GoogleAuthOutcome.LOADING, delayMs = 0))

        assertTrue(result is AuthResult.Success)
    }
}
