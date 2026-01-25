package com.darioossa.poketest.domain.usecase

import com.darioossa.poketest.data.auth.AuthRepository
import com.darioossa.poketest.domain.model.AuthResult
import com.darioossa.poketest.domain.model.GoogleAuthRequest

class LoginWithGoogleUseCase(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(request: GoogleAuthRequest): AuthResult {
        return repository.loginWithGoogle(request)
    }
}
