package com.darioossa.poketest.domain.usecase

import com.darioossa.poketest.data.repository.PokemonRepository
import com.darioossa.poketest.domain.model.PokemonDetail

class GetPokemonDetailUseCase(
    private val repository: PokemonRepository
) {
    suspend operator fun invoke(id: Int, forceRefresh: Boolean = false): PokemonDetail {
        return repository.getPokemonDetail(id, forceRefresh)
    }
}
