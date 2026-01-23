package com.darioossa.poketest.domain.usecase

import com.darioossa.poketest.data.repository.PokemonRepository

class ToggleFavoriteUseCase(
    private val repository: PokemonRepository
) {
    suspend operator fun invoke(pokemonId: Int): Boolean {
        return repository.toggleFavorite(pokemonId)
    }
}
