package com.darioossa.poketest.data.auth.local

import com.darioossa.poketest.domain.model.UserCredential

/**
 * Local source for storing and retrieving the current authenticated credential.
 */
interface AuthLocalDataSource {
    suspend fun saveCredential(credential: UserCredential): Boolean

    suspend fun getCredential(): UserCredential?

    suspend fun clearCredential()
}

/**
 * Default local data source backed by [AuthDataStore].
 */
class AuthLocalDataSourceImpl(
    private val authDataStore: AuthDataStore
) : AuthLocalDataSource {
    override suspend fun saveCredential(credential: UserCredential): Boolean {
        return authDataStore.saveCredential(credential)
    }

    override suspend fun getCredential(): UserCredential? {
        return authDataStore.getCredential()
    }

    override suspend fun clearCredential() {
        authDataStore.clearCredential()
    }
}
