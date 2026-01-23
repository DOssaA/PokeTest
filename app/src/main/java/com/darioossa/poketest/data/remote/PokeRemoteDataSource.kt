package com.darioossa.poketest.data.remote

class PokeRemoteDataSource(
    private val service: PokeApiService
) {
    suspend fun fetchPokemonList(limit: Int, offset: Int) = service.getPokemonList(limit, offset)

    suspend fun fetchPokemonDetail(idOrName: String) = service.getPokemonDetail(idOrName)

    suspend fun fetchPokemonSpecies(idOrName: String) = service.getPokemonSpecies(idOrName)
}
