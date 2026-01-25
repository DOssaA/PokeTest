package com.darioossa.poketest.domain.usecase

import com.darioossa.poketest.data.repository.PokemonRepository

class GetPokemonTypesUseCase(
    private val repository: PokemonRepository
) {
    suspend operator fun invoke(): List<String> {
        return repository.getPokemonTypes()
    }
}
