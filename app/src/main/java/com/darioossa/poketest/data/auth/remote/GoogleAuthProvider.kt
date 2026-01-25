package com.darioossa.poketest.data.auth.remote

import com.darioossa.poketest.domain.model.AuthMethod
import com.darioossa.poketest.domain.model.AuthProfile
import com.darioossa.poketest.domain.model.AuthResult
import com.darioossa.poketest.domain.model.AuthToken
import com.darioossa.poketest.domain.model.CredentialStorageState
import com.darioossa.poketest.domain.model.GoogleAuthOutcome
import com.darioossa.poketest.domain.model.GoogleAuthRequest
import com.darioossa.poketest.domain.model.UserCredential
import kotlinx.coroutines.delay
import java.util.UUID

/**
 * Local Google OAuth simulator that returns deterministic outcomes for UI testing.
 */
class GoogleAuthProvider {
    suspend fun authenticate(request: GoogleAuthRequest): AuthResult {
        return when (request.outcome) {
            GoogleAuthOutcome.SUCCESS -> successResult()
            GoogleAuthOutcome.CANCELED -> AuthResult.Canceled(
                method = AuthMethod.GOOGLE,
                message = "Sign-in canceled"
            )
            GoogleAuthOutcome.ERROR -> AuthResult.Error(
                method = AuthMethod.GOOGLE,
                code = "GOOGLE_FAILED",
                message = "Unable to reach Google"
            )
            GoogleAuthOutcome.LOADING -> {
                val delayMs = request.delayMs.coerceAtLeast(0)
                if (delayMs > 0) {
                    delay(delayMs)
                }
                successResult()
            }
        }
    }

    private fun successResult(): AuthResult.Success {
        val token = AuthToken(accessToken = "fake-access-token")
        val profile = AuthProfile(
            displayName = "Poke Trainer",
            email = "trainer@poketest.local",
            avatarUrl = null
        )
        val credential = UserCredential(
            credentialId = UUID.randomUUID().toString(),
            authMethod = AuthMethod.GOOGLE,
            username = null,
            secret = token.accessToken,
            profile = profile,
            lastAuthenticatedAt = System.currentTimeMillis(),
            storageState = CredentialStorageState.FAILED
        )
        return AuthResult.Success(
            method = AuthMethod.GOOGLE,
            credential = credential,
            token = token,
            profile = profile
        )
    }
}
