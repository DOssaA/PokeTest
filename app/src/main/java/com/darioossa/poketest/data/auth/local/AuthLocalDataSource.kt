package com.darioossa.poketest.data.auth.local

import com.darioossa.poketest.domain.model.UserCredential

interface AuthLocalDataSource {
    suspend fun saveCredential(credential: UserCredential): Boolean

    suspend fun getCredential(): UserCredential?

    suspend fun clearCredential()
}

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
