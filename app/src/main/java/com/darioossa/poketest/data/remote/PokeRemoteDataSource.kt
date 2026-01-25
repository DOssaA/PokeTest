package com.darioossa.poketest.data.remote

import com.darioossa.poketest.data.remote.dto.PokemonDetailDto
import com.darioossa.poketest.data.remote.dto.PokemonListResponseDto
import com.darioossa.poketest.data.remote.dto.PokemonTypeListResponseDto
import com.darioossa.poketest.data.remote.dto.PokemonSpeciesDto

interface PokeRemoteDataSource {
    suspend fun fetchPokemonList(limit: Int, offset: Int): PokemonListResponseDto

    suspend fun fetchPokemonDetail(idOrName: String): PokemonDetailDto

    suspend fun fetchPokemonSpecies(idOrName: String): PokemonSpeciesDto

    suspend fun fetchPokemonTypes(limit: Int, offset: Int): PokemonTypeListResponseDto
}

class PokeRemoteDataSourceImpl(
    private val service: PokeApiService
) : PokeRemoteDataSource {
    override suspend fun fetchPokemonList(limit: Int, offset: Int) =
        service.getPokemonList(limit, offset)

    override suspend fun fetchPokemonDetail(idOrName: String) =
        service.getPokemonDetail(idOrName)

    override suspend fun fetchPokemonSpecies(idOrName: String) =
        service.getPokemonSpecies(idOrName)

    override suspend fun fetchPokemonTypes(limit: Int, offset: Int) =
        service.getPokemonTypes(limit, offset)
}
