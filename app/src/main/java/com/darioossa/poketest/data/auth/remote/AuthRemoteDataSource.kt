package com.darioossa.poketest.data.auth.remote

import com.darioossa.poketest.domain.model.AuthResult
import com.darioossa.poketest.domain.model.BiometricAuthRequest
import com.darioossa.poketest.domain.model.GoogleAuthRequest
import com.darioossa.poketest.util.biometric.BiometricPromptManager

/**
 * Remote auth source for biometric and Google sign-in outcomes.
 */
interface AuthRemoteDataSource {
    suspend fun authenticateBiometric(request: BiometricAuthRequest): AuthResult

    suspend fun authenticateGoogle(request: GoogleAuthRequest): AuthResult
}

/**
 * Simulated remote data source implementation delegating to local prompt/provider helpers.
 */
class AuthRemoteDataSourceImpl(
    private val biometricPromptManager: BiometricPromptManager,
    private val googleAuthProvider: GoogleAuthProvider
) : AuthRemoteDataSource {
    override suspend fun authenticateBiometric(request: BiometricAuthRequest): AuthResult {
        return biometricPromptManager.authenticate(request)
    }

    override suspend fun authenticateGoogle(request: GoogleAuthRequest): AuthResult {
        return googleAuthProvider.authenticate(request)
    }
}
