package com.darioossa.poketest.domain.usecase

import com.darioossa.poketest.data.auth.AuthRepository
import com.darioossa.poketest.domain.model.AuthMethod
import com.darioossa.poketest.domain.model.AuthProfile
import com.darioossa.poketest.domain.model.AuthResult
import com.darioossa.poketest.domain.model.AuthToken
import com.darioossa.poketest.domain.model.CredentialStorageState
import com.darioossa.poketest.domain.model.GoogleAuthOutcome
import com.darioossa.poketest.domain.model.GoogleAuthRequest
import com.darioossa.poketest.domain.model.UserCredential
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class LoginWithGoogleUseCaseTest {
    @Test
    fun `invoke returns repository result`() = runTest {
        val repository = mockk<AuthRepository>()
        val request = GoogleAuthRequest(outcome = GoogleAuthOutcome.SUCCESS)
        val credential = UserCredential(
            credentialId = "cred-4",
            authMethod = AuthMethod.GOOGLE,
            username = null,
            secret = "token",
            profile = AuthProfile("Ash", "ash@example.com", null),
            lastAuthenticatedAt = 456L,
            storageState = CredentialStorageState.STORED
        )
        val expected = AuthResult.Success(
            method = AuthMethod.GOOGLE,
            credential = credential,
            token = AuthToken(accessToken = "token")
        )
        coEvery { repository.loginWithGoogle(request) } returns expected

        val useCase = LoginWithGoogleUseCase(repository)

        val result = useCase(request)

        assertEquals(expected, result)
    }

    @Test
    fun `invoke returns repository error`() = runTest {
        val repository = mockk<AuthRepository>()
        val request = GoogleAuthRequest(outcome = GoogleAuthOutcome.ERROR)
        val expected = AuthResult.Error(
            method = AuthMethod.GOOGLE,
            code = "GOOGLE_FAILED",
            message = "failed"
        )
        coEvery { repository.loginWithGoogle(request) } returns expected

        val useCase = LoginWithGoogleUseCase(repository)

        val result = useCase(request)

        assertEquals(expected, result)
    }
}
