package com.darioossa.poketest.domain.model

enum class AuthMethod {
    PASSWORD,
    BIOMETRIC,
    GOOGLE
}

enum class AuthOutcome {
    SUCCESS,
    CANCELED,
    ERROR,
    LOADING
}

enum class CredentialStorageState {
    STORED,
    FAILED
}

data class AuthToken(
    val accessToken: String,
    val tokenType: String = "Bearer",
    val expiresInSeconds: Long? = null
)

data class AuthProfile(
    val displayName: String?,
    val email: String?,
    val avatarUrl: String?
)

data class UserCredential(
    val credentialId: String,
    val authMethod: AuthMethod,
    val username: String?,
    val secret: String?,
    val profile: AuthProfile?,
    val lastAuthenticatedAt: Long,
    val storageState: CredentialStorageState
)

data class AuthenticationEvent(
    val eventId: String,
    val authMethod: AuthMethod,
    val outcome: AuthOutcome,
    val occurredAt: Long,
    val errorReason: String? = null
)

data class BiometricAuthRequest(
    val title: String,
    val subtitle: String? = null,
    val description: String? = null
)

enum class GoogleAuthOutcome {
    SUCCESS,
    CANCELED,
    ERROR,
    LOADING
}

data class GoogleAuthRequest(
    val outcome: GoogleAuthOutcome,
    val delayMs: Long = 0L
)

sealed class AuthResult {
    data class Success(
        val method: AuthMethod,
        val credential: UserCredential,
        val token: AuthToken? = null,
        val profile: AuthProfile? = null
    ) : AuthResult()

    data class Canceled(
        val method: AuthMethod,
        val message: String
    ) : AuthResult()

    data class Error(
        val method: AuthMethod,
        val code: String,
        val message: String
    ) : AuthResult()

    data class Loading(
        val method: AuthMethod,
        val retryAfterMs: Long? = null
    ) : AuthResult()
}
