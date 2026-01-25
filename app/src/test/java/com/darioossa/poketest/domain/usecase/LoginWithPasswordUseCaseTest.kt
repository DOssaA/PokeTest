package com.darioossa.poketest.domain.usecase

import com.darioossa.poketest.data.auth.AuthRepository
import com.darioossa.poketest.domain.model.AuthMethod
import com.darioossa.poketest.domain.model.AuthResult
import com.darioossa.poketest.domain.model.CredentialStorageState
import com.darioossa.poketest.domain.model.UserCredential
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class LoginWithPasswordUseCaseTest {
    @Test
    fun `invoke returns repository result`() = runTest {
        val repository = mockk<AuthRepository>()
        val credential = UserCredential(
            credentialId = "cred-1",
            authMethod = AuthMethod.PASSWORD,
            username = "ash",
            secret = "pikachu",
            profile = null,
            lastAuthenticatedAt = 123L,
            storageState = CredentialStorageState.STORED
        )
        val expected = AuthResult.Success(
            method = AuthMethod.PASSWORD,
            credential = credential
        )
        coEvery { repository.loginWithPassword("ash", "pikachu") } returns expected

        val useCase = LoginWithPasswordUseCase(repository)

        val result = useCase("ash", "pikachu")

        assertEquals(expected, result)
    }

    @Test
    fun `invoke returns repository error`() = runTest {
        val repository = mockk<AuthRepository>()
        val expected = AuthResult.Error(
            method = AuthMethod.PASSWORD,
            code = "INVALID_CREDENTIALS",
            message = "invalid"
        )
        coEvery { repository.loginWithPassword("ash", "bad") } returns expected

        val useCase = LoginWithPasswordUseCase(repository)

        val result = useCase("ash", "bad")

        assertEquals(expected, result)
    }
}
