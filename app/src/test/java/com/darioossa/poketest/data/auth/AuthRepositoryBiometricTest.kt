package com.darioossa.poketest.data.auth

import com.darioossa.poketest.data.auth.local.AuthLocalDataSource
import com.darioossa.poketest.data.auth.remote.AuthRemoteDataSource
import com.darioossa.poketest.domain.model.AuthMethod
import com.darioossa.poketest.domain.model.AuthResult
import com.darioossa.poketest.domain.model.BiometricAuthRequest
import com.darioossa.poketest.domain.model.CredentialStorageState
import com.darioossa.poketest.domain.model.UserCredential
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class AuthRepositoryBiometricTest {
    @Test
    fun loginWithBiometricDelegatesToRemote() = runTest {
        val localDataSource = mockk<AuthLocalDataSource>()
        val remoteDataSource = mockk<AuthRemoteDataSource>()
        val request = BiometricAuthRequest(title = "Sign in")
        val credential = UserCredential(
            credentialId = "cred-3",
            authMethod = AuthMethod.BIOMETRIC,
            username = null,
            secret = null,
            profile = null,
            lastAuthenticatedAt = 321L,
            storageState = CredentialStorageState.STORED
        )
        val expected = AuthResult.Success(
            method = AuthMethod.BIOMETRIC,
            credential = credential
        )
        coEvery { remoteDataSource.authenticateBiometric(request) } returns expected
        coEvery { localDataSource.saveCredential(any()) } returns true

        val repository = AuthRepositoryImpl(localDataSource, remoteDataSource)

        val result = repository.loginWithBiometric(request)

        assertTrue(result is AuthResult.Success)
        val success = result as AuthResult.Success
        assertEquals(AuthMethod.BIOMETRIC, success.method)
        assertEquals(CredentialStorageState.STORED, success.credential.storageState)
        coVerify(exactly = 1) { remoteDataSource.authenticateBiometric(request) }
        coVerify(exactly = 1) { localDataSource.saveCredential(any()) }
    }

    @Test
    fun loginWithBiometricReturnsStorageErrorWhenSaveFails() = runTest {
        val localDataSource = mockk<AuthLocalDataSource>()
        val remoteDataSource = mockk<AuthRemoteDataSource>()
        val request = BiometricAuthRequest(title = "Sign in")
        val credential = UserCredential(
            credentialId = "cred-4",
            authMethod = AuthMethod.BIOMETRIC,
            username = null,
            secret = null,
            profile = null,
            lastAuthenticatedAt = 321L,
            storageState = CredentialStorageState.STORED
        )
        val expected = AuthResult.Success(
            method = AuthMethod.BIOMETRIC,
            credential = credential
        )
        coEvery { remoteDataSource.authenticateBiometric(request) } returns expected
        coEvery { localDataSource.saveCredential(any()) } returns false

        val repository = AuthRepositoryImpl(localDataSource, remoteDataSource)

        val result = repository.loginWithBiometric(request)

        assertTrue(result is AuthResult.Error)
        val error = result as AuthResult.Error
        assertEquals("STORAGE_FAILED", error.code)
    }
}
