package com.darioossa.poketest.domain.usecase

import com.darioossa.poketest.data.auth.AuthRepository
import com.darioossa.poketest.domain.model.AuthMethod
import com.darioossa.poketest.domain.model.AuthResult
import com.darioossa.poketest.domain.model.BiometricAuthRequest
import com.darioossa.poketest.domain.model.CredentialStorageState
import com.darioossa.poketest.domain.model.UserCredential
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class LoginWithBiometricUseCaseTest {
    @Test
    fun `invoke returns repository result`() = runTest {
        val repository = mockk<AuthRepository>()
        val request = BiometricAuthRequest(title = "Sign in")
        val credential = UserCredential(
            credentialId = "cred-2",
            authMethod = AuthMethod.BIOMETRIC,
            username = null,
            secret = null,
            profile = null,
            lastAuthenticatedAt = 123L,
            storageState = CredentialStorageState.STORED
        )
        val expected = AuthResult.Success(
            method = AuthMethod.BIOMETRIC,
            credential = credential
        )
        coEvery { repository.loginWithBiometric(request) } returns expected

        val useCase = LoginWithBiometricUseCase(repository)

        val result = useCase(request)

        assertEquals(expected, result)
    }

    @Test
    fun `invoke returns repository error`() = runTest {
        val repository = mockk<AuthRepository>()
        val request = BiometricAuthRequest(title = "Sign in")
        val expected = AuthResult.Error(
            method = AuthMethod.BIOMETRIC,
            code = "BIOMETRIC_FAILED",
            message = "failed"
        )
        coEvery { repository.loginWithBiometric(request) } returns expected

        val useCase = LoginWithBiometricUseCase(repository)

        val result = useCase(request)

        assertEquals(expected, result)
    }
}
