package com.darioossa.poketest.ui.auth

import androidx.lifecycle.viewModelScope
import com.darioossa.poketest.domain.model.BiometricAuthRequest
import com.darioossa.poketest.domain.model.GoogleAuthOutcome
import com.darioossa.poketest.domain.model.GoogleAuthRequest
import com.darioossa.poketest.domain.usecase.LoginWithBiometricUseCase
import com.darioossa.poketest.domain.usecase.LoginWithGoogleUseCase
import com.darioossa.poketest.domain.usecase.LoginWithPasswordUseCase
import com.darioossa.poketest.ui.base.BaseMVIViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LoginViewModel(
    private val loginWithPasswordUseCase: LoginWithPasswordUseCase,
    private val loginWithBiometricUseCase: LoginWithBiometricUseCase,
    private val loginWithGoogleUseCase: LoginWithGoogleUseCase,
    reducer: LoginReducer,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : BaseMVIViewModel<LoginState, LoginEvent, LoginEffect>(
    initialState = LoginState(),
    reducer = reducer
) {
    fun onUsernameChange(value: String) {
        sendEvent(LoginEvent.UsernameChanged(value))
    }

    fun onPasswordChange(value: String) {
        sendEvent(LoginEvent.PasswordChanged(value))
    }

    fun onSubmit() {
        sendEvent(LoginEvent.Submit)
        val current = state.value
        if (current.isSubmitting && current.error == null) {
            viewModelScope.launch(dispatcher) {
                val result = loginWithPasswordUseCase(
                    username = current.username.trim(),
                    password = current.password
                )
                sendEvent(LoginEvent.AuthResultReceived(result))
            }
        }
    }

    fun onBiometricClick() {
        sendEvent(LoginEvent.BiometricClicked)
        val current = state.value
        if (current.isSubmitting && current.error == null) {
            viewModelScope.launch(dispatcher) {
                val result = withContext(Dispatchers.Main) {
                    loginWithBiometricUseCase(
                        BiometricAuthRequest(
                            title = "Unlock PokeTest",
                            subtitle = "Sign in with biometrics",
                            description = "Use fingerprint or face to continue"
                        )
                    )
                }
                sendEvent(LoginEvent.AuthResultReceived(result))
            }
        }
    }

    fun onGoogleClick() {
        sendEvent(LoginEvent.GoogleClicked)
    }

    fun onGoogleContinue(outcome: GoogleAuthOutcome, delayMs: Long = 0L) {
        sendEvent(LoginEvent.GoogleSubmit)
        val current = state.value
        if (current.isSubmitting && current.error == null) {
            viewModelScope.launch(dispatcher) {
                val result = loginWithGoogleUseCase(GoogleAuthRequest(outcome = outcome, delayMs = delayMs))
                sendEvent(LoginEvent.AuthResultReceived(result))
            }
        }
    }

    fun onGoogleCancel() {
        onGoogleContinue(outcome = GoogleAuthOutcome.CANCELED)
    }

    fun onGoogleError() {
        onGoogleContinue(outcome = GoogleAuthOutcome.ERROR)
    }

    fun clearError() {
        sendEvent(LoginEvent.ClearError)
    }
}
