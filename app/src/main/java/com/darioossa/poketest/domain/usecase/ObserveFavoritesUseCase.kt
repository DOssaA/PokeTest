package com.darioossa.poketest.domain.usecase

import com.darioossa.poketest.data.repository.PokemonRepository
import kotlinx.coroutines.flow.Flow

class ObserveFavoritesUseCase(
    private val repository: PokemonRepository
) {
    operator fun invoke(): Flow<Set<Int>> {
        return repository.observeFavorites()
    }
}
