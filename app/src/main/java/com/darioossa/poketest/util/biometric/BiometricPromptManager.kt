package com.darioossa.poketest.util.biometric

import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import com.darioossa.poketest.domain.model.AuthMethod
import com.darioossa.poketest.domain.model.AuthResult
import com.darioossa.poketest.domain.model.BiometricAuthRequest
import com.darioossa.poketest.domain.model.CredentialStorageState
import com.darioossa.poketest.domain.model.UserCredential
import java.lang.ref.WeakReference
import java.util.UUID
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

/**
 * Coordinates AndroidX BiometricPrompt authentication for the login flow.
 *
 * This helper binds to a [FragmentActivity] to display the system biometric prompt and
 * exposes a suspend API that maps prompt callbacks into [AuthResult] outcomes.
 *
 * Expected outcomes:
 * - [AuthResult.Success] when authentication succeeds
 * - [AuthResult.Canceled] when the user dismisses the prompt
 * - [AuthResult.Error] for other errors or when biometrics are unavailable
 */
class BiometricPromptManager {
    private var activityRef: WeakReference<FragmentActivity>? = null

    /**
     * Binds the prompt manager to the current [FragmentActivity].
     *
     * Must be called before [authenticate] so the prompt can be displayed.
     */
    fun bind(activity: FragmentActivity) {
        activityRef = WeakReference(activity)
    }

    /**
     * Triggers the system biometric prompt and returns the authentication outcome.
     *
     * The [BiometricAuthRequest] supplies the title/subtitle/description used in the prompt.
     */
    suspend fun authenticate(request: BiometricAuthRequest): AuthResult {
        val activity = activityRef?.get()
            ?: return AuthResult.Error(
                method = AuthMethod.BIOMETRIC,
                code = "BIOMETRIC_UNAVAILABLE",
                message = "Biometric host not available"
            )

        val authenticators = BiometricManager.Authenticators.BIOMETRIC_STRONG or
            BiometricManager.Authenticators.BIOMETRIC_WEAK
        val biometricManager = BiometricManager.from(activity)
        val canAuthenticate = biometricManager.canAuthenticate(authenticators)
        if (canAuthenticate != BiometricManager.BIOMETRIC_SUCCESS) {
            return AuthResult.Error(
                method = AuthMethod.BIOMETRIC,
                code = "BIOMETRIC_UNAVAILABLE",
                message = "Biometric authentication unavailable"
            )
        }

        return suspendCoroutine { continuation ->
            var resumed = false
            val executor = ContextCompat.getMainExecutor(activity)
            val prompt = BiometricPrompt(
                activity,
                executor,
                object : BiometricPrompt.AuthenticationCallback() {
                    override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                        val credential = UserCredential(
                            credentialId = UUID.randomUUID().toString(),
                            authMethod = AuthMethod.BIOMETRIC,
                            username = null,
                            secret = null,
                            profile = null,
                            lastAuthenticatedAt = System.currentTimeMillis(),
                            storageState = CredentialStorageState.FAILED
                        )
                        if (!resumed) {
                            resumed = true
                            continuation.resume(
                                AuthResult.Success(
                                    method = AuthMethod.BIOMETRIC,
                                    credential = credential
                                )
                            )
                        }
                    }

                    override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                        val canceled = errorCode == BiometricPrompt.ERROR_USER_CANCELED ||
                            errorCode == BiometricPrompt.ERROR_NEGATIVE_BUTTON ||
                            errorCode == BiometricPrompt.ERROR_CANCELED
                        if (resumed) return
                        resumed = true
                        if (canceled) {
                            continuation.resume(
                                AuthResult.Canceled(
                                    method = AuthMethod.BIOMETRIC,
                                    message = errString.toString()
                                )
                            )
                        } else {
                            continuation.resume(
                                AuthResult.Error(
                                    method = AuthMethod.BIOMETRIC,
                                    code = "BIOMETRIC_FAILED",
                                    message = errString.toString()
                                )
                            )
                        }
                    }
                }
            )

            val builder = BiometricPrompt.PromptInfo.Builder()
                .setTitle(request.title)
                .setAllowedAuthenticators(authenticators)
                .setConfirmationRequired(false)
            request.subtitle?.takeIf { it.isNotBlank() }?.let { builder.setSubtitle(it) }
            request.description?.takeIf { it.isNotBlank() }?.let { builder.setDescription(it) }
            val promptInfo = builder.build()

            prompt.authenticate(promptInfo)
        }
    }
}
