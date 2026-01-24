package com.darioossa.poketest.data.auth

import com.darioossa.poketest.domain.model.AuthResult
import com.darioossa.poketest.domain.model.BiometricAuthRequest
import com.darioossa.poketest.domain.model.GoogleAuthRequest

interface AuthRepository {
    suspend fun loginWithPassword(username: String, password: String): AuthResult

    suspend fun loginWithBiometric(request: BiometricAuthRequest): AuthResult

    suspend fun loginWithGoogle(request: GoogleAuthRequest): AuthResult
}
