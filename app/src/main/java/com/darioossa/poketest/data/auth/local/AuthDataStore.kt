package com.darioossa.poketest.data.auth.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.darioossa.poketest.data.auth.crypto.AuthCryptoManager
import com.darioossa.poketest.domain.model.AuthMethod
import com.darioossa.poketest.domain.model.AuthProfile
import com.darioossa.poketest.domain.model.CredentialStorageState
import com.darioossa.poketest.domain.model.UserCredential
import com.squareup.moshi.JsonClass
import com.squareup.moshi.Moshi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

private val Context.authDataStore: DataStore<Preferences> by preferencesDataStore(name = "auth")

/**
 * Encrypted DataStore wrapper for persisting a single authenticated credential.
 */
class AuthDataStore(
    private val context: Context,
    private val cryptoManager: AuthCryptoManager,
    moshi: Moshi
) {
    /**
     * Stores a single encrypted credential payload using DataStore.
     *
     * The credential JSON is encrypted with a Keystore-backed AES/GCM key
     * before persistence and never written in plain text.
     */
    private val credentialKey = stringPreferencesKey("auth_credentials_encrypted")
    private val adapter = moshi.adapter(StoredCredential::class.java)

    suspend fun saveCredential(credential: UserCredential): Boolean {
        return runCatching {
            val stored = credential.toStored()
            val json = adapter.toJson(stored)
            val encrypted = cryptoManager.encrypt(json)
            context.authDataStore.edit { prefs ->
                prefs[credentialKey] = encrypted
            }
        }.isSuccess
    }

    suspend fun getCredential(): UserCredential? {
        val encrypted = context.authDataStore.data
            .map { prefs -> prefs[credentialKey].orEmpty() }
            .first()
        if (encrypted.isBlank()) return null
        val decrypted = cryptoManager.decrypt(encrypted) ?: return null
        val stored = adapter.fromJson(decrypted) ?: return null
        return stored.toDomain()
    }

    suspend fun clearCredential() {
        context.authDataStore.edit { prefs ->
            prefs.remove(credentialKey)
        }
    }

    private fun UserCredential.toStored() = StoredCredential(
        credentialId = credentialId,
        authMethod = authMethod.name,
        username = username,
        secret = secret,
        profile = profile?.let {
            StoredProfile(
                displayName = it.displayName,
                email = it.email,
                avatarUrl = it.avatarUrl
            )
        },
        lastAuthenticatedAt = lastAuthenticatedAt
    )

    private fun StoredCredential.toDomain() = UserCredential(
        credentialId = credentialId,
        authMethod = AuthMethod.valueOf(authMethod),
        username = username,
        secret = secret,
        profile = profile?.toDomain(),
        lastAuthenticatedAt = lastAuthenticatedAt,
        storageState = CredentialStorageState.STORED
    )

    private fun StoredProfile.toDomain() = AuthProfile(
        displayName = displayName,
        email = email,
        avatarUrl = avatarUrl
    )

    @JsonClass(generateAdapter = true)
    data class StoredCredential(
        val credentialId: String,
        val authMethod: String,
        val username: String?,
        val secret: String?,
        val profile: StoredProfile?,
        val lastAuthenticatedAt: Long
    )

    @JsonClass(generateAdapter = true)
    data class StoredProfile(
        val displayName: String?,
        val email: String?,
        val avatarUrl: String?
    )
}
