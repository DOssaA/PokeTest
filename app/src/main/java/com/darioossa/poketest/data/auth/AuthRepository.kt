package com.darioossa.poketest.data.auth

import com.darioossa.poketest.domain.model.AuthResult
import com.darioossa.poketest.domain.model.BiometricAuthRequest
import com.darioossa.poketest.domain.model.GoogleAuthRequest

/**
 * Provides authentication operations for the app using simulated providers.
 *
 * Implementations are responsible for persisting credentials securely after success.
 */
interface AuthRepository {
    suspend fun loginWithPassword(username: String, password: String): AuthResult

    suspend fun loginWithBiometric(request: BiometricAuthRequest): AuthResult

    suspend fun loginWithGoogle(request: GoogleAuthRequest): AuthResult
}
