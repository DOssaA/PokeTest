package com.darioossa.poketest.domain.usecase

import com.darioossa.poketest.data.repository.PokemonRepository
import com.darioossa.poketest.domain.model.PokemonSummary

class GetPokemonListUseCase(
    private val repository: PokemonRepository
) {
    suspend operator fun invoke(limit: Int, offset: Int, forceRefresh: Boolean = false): List<PokemonSummary> {
        return repository.getPokemonList(limit, offset, forceRefresh)
    }
}
