package com.darioossa.poketest.domain.usecase

import com.darioossa.poketest.data.auth.AuthRepository
import com.darioossa.poketest.domain.model.AuthResult
import com.darioossa.poketest.domain.model.BiometricAuthRequest

class LoginWithBiometricUseCase(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(request: BiometricAuthRequest): AuthResult {
        return repository.loginWithBiometric(request)
    }
}
