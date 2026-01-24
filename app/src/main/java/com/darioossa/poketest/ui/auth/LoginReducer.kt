package com.darioossa.poketest.ui.auth

import com.darioossa.poketest.domain.Reducer
import androidx.annotation.StringRes
import com.darioossa.poketest.R
import com.darioossa.poketest.domain.model.AuthMethod
import com.darioossa.poketest.domain.model.AuthResult

class LoginReducer : Reducer<LoginState, LoginEvent, LoginEffect> {
    override fun reduce(previousState: LoginState, event: LoginEvent): Pair<LoginState, LoginEffect?> {
        return when (event) {
            is LoginEvent.UsernameChanged -> {
                previousState.copy(username = event.value, error = null) to null
            }
            is LoginEvent.PasswordChanged -> {
                previousState.copy(password = event.value, error = null) to null
            }
            LoginEvent.Submit -> {
                when {
                    previousState.username.isBlank() -> {
                        previousState.copy(isSubmitting = false, error = LoginError.MissingUsername) to null
                    }
                    previousState.password.isBlank() -> {
                        previousState.copy(isSubmitting = false, error = LoginError.MissingPassword) to null
                    }
                    else -> {
                        previousState.copy(isSubmitting = true, error = null) to null
                    }
                }
            }
            is LoginEvent.AuthResultReceived -> {
                val nextScreen = when (event.result) {
                    is AuthResult.Success -> LoginScreenStep.LOGIN
                    is AuthResult.Error -> if (event.result.method == AuthMethod.GOOGLE) {
                        LoginScreenStep.LOGIN
                    } else {
                        previousState.screen
                    }
                    is AuthResult.Canceled -> if (event.result.method == AuthMethod.GOOGLE) {
                        LoginScreenStep.LOGIN
                    } else {
                        previousState.screen
                    }
                    is AuthResult.Loading -> previousState.screen
                }
                when (val result = event.result) {
                    is AuthResult.Success -> {
                        previousState.copy(isSubmitting = false, error = null, screen = nextScreen) to LoginEffect.NavigateHome
                    }
                    is AuthResult.Canceled -> {
                        previousState.copy(isSubmitting = false, error = LoginError.Canceled(result.message), screen = nextScreen) to null
                    }
                    is AuthResult.Error -> {
                        previousState.copy(isSubmitting = false, error = LoginError.fromError(result), screen = nextScreen) to null
                    }
                    is AuthResult.Loading -> {
                        previousState.copy(isSubmitting = true, error = null) to null
                    }
                }
            }
            LoginEvent.BiometricClicked -> {
                previousState.copy(isSubmitting = true, error = null) to null
            }
            LoginEvent.GoogleClicked -> {
                previousState.copy(screen = LoginScreenStep.GOOGLE, error = null, isSubmitting = false) to null
            }
            LoginEvent.GoogleSubmit -> {
                previousState.copy(isSubmitting = true, error = null) to null
            }
            LoginEvent.ClearError -> {
                previousState.copy(error = null) to null
            }
        }
    }
}

data class LoginState(
    val username: String = "",
    val password: String = "",
    val isSubmitting: Boolean = false,
    val error: LoginError? = null,
    val screen: LoginScreenStep = LoginScreenStep.LOGIN
) : Reducer.ViewState

enum class LoginScreenStep {
    LOGIN,
    GOOGLE
}

sealed interface LoginEvent : Reducer.ViewEvent {
    data class UsernameChanged(val value: String) : LoginEvent
    data class PasswordChanged(val value: String) : LoginEvent
    data object Submit : LoginEvent
    data class AuthResultReceived(val result: AuthResult) : LoginEvent
    data object BiometricClicked : LoginEvent
    data object GoogleClicked : LoginEvent
    data object GoogleSubmit : LoginEvent
    data object ClearError : LoginEvent
}

sealed interface LoginEffect : Reducer.ViewEffect {
    data object NavigateHome : LoginEffect
}

sealed interface LoginError {
    @get:StringRes
    val messageResId: Int
    val fallbackMessage: String?

    data object MissingUsername : LoginError {
        override val messageResId: Int = R.string.login_error_missing_username
        override val fallbackMessage: String? = null
    }

    data object MissingPassword : LoginError {
        override val messageResId: Int = R.string.login_error_missing_password
        override val fallbackMessage: String? = null
    }

    data object InvalidCredentials : LoginError {
        override val messageResId: Int = R.string.login_error_invalid_credentials
        override val fallbackMessage: String? = null
    }

    data object StorageFailed : LoginError {
        override val messageResId: Int = R.string.login_error_storage_failed
        override val fallbackMessage: String? = null
    }

    data object BiometricFailed : LoginError {
        override val messageResId: Int = R.string.login_error_biometric_failed
        override val fallbackMessage: String? = null
    }

    data object GoogleFailed : LoginError {
        override val messageResId: Int = R.string.login_error_google_failed
        override val fallbackMessage: String? = null
    }

    data class Canceled(override val fallbackMessage: String) : LoginError {
        override val messageResId: Int = R.string.login_error_generic
    }

    data class Unknown(override val fallbackMessage: String) : LoginError {
        override val messageResId: Int = R.string.login_error_generic
    }

    data object BiometricNotReady : LoginError {
        override val messageResId: Int = R.string.login_error_biometric_unavailable
        override val fallbackMessage: String? = null
    }

    data object GoogleNotReady : LoginError {
        override val messageResId: Int = R.string.login_error_google_unavailable
        override val fallbackMessage: String? = null
    }

    companion object {
        fun fromError(result: AuthResult.Error): LoginError {
            return when (result.code) {
                "INVALID_CREDENTIALS" -> InvalidCredentials
                "STORAGE_FAILED" -> StorageFailed
                "BIOMETRIC_FAILED" -> BiometricFailed
                "BIOMETRIC_UNAVAILABLE" -> BiometricNotReady
                "GOOGLE_FAILED" -> GoogleFailed
                else -> Unknown(result.message)
            }
        }
    }
}
