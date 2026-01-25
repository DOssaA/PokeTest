package com.darioossa.poketest.domain.usecase

import com.darioossa.poketest.data.auth.AuthRepository
import com.darioossa.poketest.domain.model.AuthResult

class LoginWithPasswordUseCase(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(username: String, password: String): AuthResult {
        return repository.loginWithPassword(username, password)
    }
}
