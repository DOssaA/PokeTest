package com.darioossa.poketest.data.auth

import com.darioossa.poketest.data.auth.local.AuthLocalDataSource
import com.darioossa.poketest.data.auth.remote.AuthRemoteDataSource
import com.darioossa.poketest.domain.model.AuthMethod
import com.darioossa.poketest.domain.model.AuthResult
import com.darioossa.poketest.domain.model.CredentialStorageState
import com.darioossa.poketest.domain.model.BiometricAuthRequest
import com.darioossa.poketest.domain.model.UserCredential
import com.darioossa.poketest.domain.model.GoogleAuthRequest
import java.util.UUID

/**
 * Default auth repository that coordinates local storage with simulated remote providers.
 */
class AuthRepositoryImpl(
    private val localDataSource: AuthLocalDataSource,
    private val remoteDataSource: AuthRemoteDataSource
) : AuthRepository {
    override suspend fun loginWithPassword(username: String, password: String): AuthResult {
        if (username.isBlank() || password.isBlank()) {
            return AuthResult.Error(
                method = AuthMethod.PASSWORD,
                code = "INVALID_CREDENTIALS",
                message = "Username and password are required"
            )
        }

        val credential = UserCredential(
            credentialId = UUID.randomUUID().toString(),
            authMethod = AuthMethod.PASSWORD,
            username = username.trim(),
            secret = password,
            profile = null,
            lastAuthenticatedAt = System.currentTimeMillis(),
            storageState = CredentialStorageState.STORED
        )

        val stored = localDataSource.saveCredential(credential)
        if (!stored) {
            return AuthResult.Error(
                method = AuthMethod.PASSWORD,
                code = "STORAGE_FAILED",
                message = "Failed to store credentials"
            )
        }

        return AuthResult.Success(
            method = AuthMethod.PASSWORD,
            credential = credential
        )
    }

    override suspend fun loginWithBiometric(request: BiometricAuthRequest): AuthResult {
        val result = remoteDataSource.authenticateBiometric(request)
        if (result is AuthResult.Success) {
            val storedCredential = result.credential.copy(storageState = CredentialStorageState.STORED)
            val stored = localDataSource.saveCredential(storedCredential)
            if (!stored) {
                return AuthResult.Error(
                    method = AuthMethod.BIOMETRIC,
                    code = "STORAGE_FAILED",
                    message = "Failed to store credentials"
                )
            }
            return result.copy(credential = storedCredential)
        }
        return result
    }

    override suspend fun loginWithGoogle(request: GoogleAuthRequest): AuthResult {
        val result = remoteDataSource.authenticateGoogle(request)
        if (result is AuthResult.Success) {
            val storedCredential = result.credential.copy(storageState = CredentialStorageState.STORED)
            val stored = localDataSource.saveCredential(storedCredential)
            if (!stored) {
                return AuthResult.Error(
                    method = AuthMethod.GOOGLE,
                    code = "STORAGE_FAILED",
                    message = "Failed to store credentials"
                )
            }
            return result.copy(credential = storedCredential)
        }
        return result
    }
}
