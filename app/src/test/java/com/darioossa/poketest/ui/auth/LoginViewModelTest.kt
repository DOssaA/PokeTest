package com.darioossa.poketest.ui.auth

import app.cash.turbine.test
import com.darioossa.poketest.data.auth.AuthRepository
import com.darioossa.poketest.domain.model.AuthMethod
import com.darioossa.poketest.domain.model.AuthResult
import com.darioossa.poketest.domain.model.AuthToken
import com.darioossa.poketest.domain.model.CredentialStorageState
import com.darioossa.poketest.domain.model.GoogleAuthOutcome
import com.darioossa.poketest.domain.model.GoogleAuthRequest
import com.darioossa.poketest.domain.model.UserCredential
import com.darioossa.poketest.domain.usecase.LoginWithBiometricUseCase
import com.darioossa.poketest.domain.usecase.LoginWithGoogleUseCase
import com.darioossa.poketest.domain.usecase.LoginWithPasswordUseCase
import com.darioossa.poketest.testutil.MainDispatcherRule
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.slot
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class LoginViewModelTest {
    @get:Rule
    val dispatcherRule = MainDispatcherRule()

    private val repository = mockk<AuthRepository>()
    private lateinit var viewModel: LoginViewModel

    private fun initViewModel() {
        viewModel = LoginViewModel(
            loginWithPasswordUseCase = LoginWithPasswordUseCase(repository),
            loginWithBiometricUseCase = LoginWithBiometricUseCase(repository),
            loginWithGoogleUseCase = LoginWithGoogleUseCase(repository),
            reducer = LoginReducer(),
            dispatcher = dispatcherRule.dispatcher
        )
    }

    @Test
    fun `username change updates state`() {
        initViewModel()

        viewModel.onUsernameChange("ash")

        assertEquals("ash", viewModel.state.value.username)
    }

    @Test
    fun `password change updates state`() {
        initViewModel()

        viewModel.onPasswordChange("pikachu")

        assertEquals("pikachu", viewModel.state.value.password)
    }

    @Test
    fun `submit with blank username sets error and does not call use case`() = runTest {
        initViewModel()
        viewModel.onPasswordChange("pikachu")

        viewModel.onSubmit()
        advanceUntilIdle()

        assertEquals(LoginError.MissingUsername, viewModel.state.value.error)
        coVerify(exactly = 0) { repository.loginWithPassword(any(), any()) }
    }

    @Test
    fun `submit with blank password sets error and does not call use case`() = runTest {
        initViewModel()
        viewModel.onUsernameChange("ash")

        viewModel.onSubmit()
        advanceUntilIdle()

        assertEquals(LoginError.MissingPassword, viewModel.state.value.error)
        coVerify(exactly = 0) { repository.loginWithPassword(any(), any()) }
    }

    @Test
    fun `submit with valid credentials emits navigate effect`() = runTest {
        val credential = credential(AuthMethod.PASSWORD)
        coEvery { repository.loginWithPassword("ash", "pikachu") } returns
            AuthResult.Success(AuthMethod.PASSWORD, credential, AuthToken("token"))
        initViewModel()
        viewModel.onUsernameChange("ash")
        viewModel.onPasswordChange("pikachu")

        viewModel.effect.test {
            viewModel.onSubmit()
            advanceUntilIdle()

            assertEquals(LoginEffect.NavigateHome, awaitItem())
            cancelAndIgnoreRemainingEvents()
        }

        assertFalse(viewModel.state.value.isSubmitting)
        assertNull(viewModel.state.value.error)
        coVerify(exactly = 1) { repository.loginWithPassword("ash", "pikachu") }
    }

    @Test
    fun `submit handles storage error`() = runTest {
        coEvery { repository.loginWithPassword("ash", "pikachu") } returns AuthResult.Error(
            method = AuthMethod.PASSWORD,
            code = "STORAGE_FAILED",
            message = "failed"
        )
        initViewModel()
        viewModel.onUsernameChange("ash")
        viewModel.onPasswordChange("pikachu")

        viewModel.onSubmit()
        advanceUntilIdle()

        assertEquals(LoginError.StorageFailed, viewModel.state.value.error)
        assertFalse(viewModel.state.value.isSubmitting)
    }

    @Test
    fun `submit handles invalid credentials error`() = runTest {
        coEvery { repository.loginWithPassword("ash", "bad") } returns AuthResult.Error(
            method = AuthMethod.PASSWORD,
            code = "INVALID_CREDENTIALS",
            message = "invalid"
        )
        initViewModel()
        viewModel.onUsernameChange("ash")
        viewModel.onPasswordChange("bad")

        viewModel.onSubmit()
        advanceUntilIdle()

        assertEquals(LoginError.InvalidCredentials, viewModel.state.value.error)
        assertFalse(viewModel.state.value.isSubmitting)
    }

    @Test
    fun `biometric success emits navigate effect`() = runTest {
        val credential = credential(AuthMethod.BIOMETRIC)
        coEvery { repository.loginWithBiometric(any()) } returns AuthResult.Success(
            method = AuthMethod.BIOMETRIC,
            credential = credential
        )
        initViewModel()

        viewModel.effect.test {
            viewModel.onBiometricClick()
            advanceUntilIdle()

            assertEquals(LoginEffect.NavigateHome, awaitItem())
            cancelAndIgnoreRemainingEvents()
        }

        coVerify(exactly = 1) { repository.loginWithBiometric(any()) }
    }

    @Test
    fun `biometric error updates state`() = runTest {
        coEvery { repository.loginWithBiometric(any()) } returns AuthResult.Error(
            method = AuthMethod.BIOMETRIC,
            code = "BIOMETRIC_FAILED",
            message = "failed"
        )
        initViewModel()

        viewModel.onBiometricClick()
        advanceUntilIdle()

        assertEquals(LoginError.BiometricFailed, viewModel.state.value.error)
    }

    @Test
    fun `biometric unavailable maps to not ready error`() = runTest {
        coEvery { repository.loginWithBiometric(any()) } returns AuthResult.Error(
            method = AuthMethod.BIOMETRIC,
            code = "BIOMETRIC_UNAVAILABLE",
            message = "unavailable"
        )
        initViewModel()

        viewModel.onBiometricClick()
        advanceUntilIdle()

        assertEquals(LoginError.BiometricNotReady, viewModel.state.value.error)
    }

    @Test
    fun `google click switches to google screen`() {
        initViewModel()

        viewModel.onGoogleClick()

        assertEquals(LoginScreenStep.GOOGLE, viewModel.state.value.screen)
    }

    @Test
    fun `google continue success emits navigate effect`() = runTest {
        val credential = credential(AuthMethod.GOOGLE)
        coEvery { repository.loginWithGoogle(any()) } returns AuthResult.Success(
            method = AuthMethod.GOOGLE,
            credential = credential,
            token = AuthToken("token")
        )
        initViewModel()
        viewModel.onGoogleClick()

        viewModel.effect.test {
            viewModel.onGoogleContinue(GoogleAuthOutcome.SUCCESS)
            advanceUntilIdle()

            assertEquals(LoginEffect.NavigateHome, awaitItem())
            cancelAndIgnoreRemainingEvents()
        }

        assertEquals(LoginScreenStep.LOGIN, viewModel.state.value.screen)
        coVerify(exactly = 1) { repository.loginWithGoogle(any()) }
    }

    @Test
    fun `google continue passes delay to repository`() = runTest {
        val requestSlot = slot<GoogleAuthRequest>()
        coEvery { repository.loginWithGoogle(capture(requestSlot)) } returns AuthResult.Loading(AuthMethod.GOOGLE, 1200)
        initViewModel()
        viewModel.onGoogleClick()

        viewModel.onGoogleContinue(GoogleAuthOutcome.LOADING, delayMs = 1200)
        advanceUntilIdle()

        assertEquals(GoogleAuthOutcome.LOADING, requestSlot.captured.outcome)
        assertEquals(1200, requestSlot.captured.delayMs)
    }

    @Test
    fun `google cancel returns to login with message`() = runTest {
        coEvery { repository.loginWithGoogle(any()) } returns AuthResult.Canceled(
            method = AuthMethod.GOOGLE,
            message = "cancel"
        )
        initViewModel()
        viewModel.onGoogleClick()

        viewModel.onGoogleContinue(GoogleAuthOutcome.CANCELED)
        advanceUntilIdle()

        assertTrue(viewModel.state.value.error is LoginError.Canceled)
        assertEquals(LoginScreenStep.LOGIN, viewModel.state.value.screen)
    }

    @Test
    fun `google loading keeps submitting`() = runTest {
        coEvery { repository.loginWithGoogle(any()) } returns AuthResult.Loading(AuthMethod.GOOGLE, 300)
        initViewModel()
        viewModel.onGoogleClick()

        viewModel.onGoogleContinue(GoogleAuthOutcome.LOADING)
        advanceUntilIdle()

        assertTrue(viewModel.state.value.isSubmitting)
    }

    @Test
    fun `submit trims username before repository call`() = runTest {
        coEvery { repository.loginWithPassword(any(), any()) } returns AuthResult.Error(
            method = AuthMethod.PASSWORD,
            code = "INVALID_CREDENTIALS",
            message = "invalid"
        )
        initViewModel()
        viewModel.onUsernameChange(" ash ")
        viewModel.onPasswordChange("pikachu")

        viewModel.onSubmit()
        advanceUntilIdle()

        coVerify(exactly = 1) { repository.loginWithPassword("ash", "pikachu") }
    }

    private fun credential(method: AuthMethod): UserCredential {
        return UserCredential(
            credentialId = "cred-${method.name}",
            authMethod = method,
            username = if (method == AuthMethod.PASSWORD) "ash" else null,
            secret = if (method == AuthMethod.PASSWORD) "pikachu" else "token",
            profile = null,
            lastAuthenticatedAt = 123L,
            storageState = CredentialStorageState.STORED
        )
    }
}
