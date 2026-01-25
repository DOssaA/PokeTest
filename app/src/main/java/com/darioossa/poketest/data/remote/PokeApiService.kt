package com.darioossa.poketest.data.remote

import com.darioossa.poketest.data.remote.dto.PokemonDetailDto
import com.darioossa.poketest.data.remote.dto.PokemonListResponseDto
import com.darioossa.poketest.data.remote.dto.PokemonTypeListResponseDto
import com.darioossa.poketest.data.remote.dto.PokemonSpeciesDto
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface PokeApiService {
    @GET("pokemon")
    suspend fun getPokemonList(
        @Query("limit") limit: Int,
        @Query("offset") offset: Int
    ): PokemonListResponseDto

    @GET("pokemon/{idOrName}")
    suspend fun getPokemonDetail(
        @Path("idOrName") idOrName: String
    ): PokemonDetailDto

    @GET("pokemon-species/{idOrName}")
    suspend fun getPokemonSpecies(
        @Path("idOrName") idOrName: String
    ): PokemonSpeciesDto

    @GET("type")
    suspend fun getPokemonTypes(
        @Query("limit") limit: Int,
        @Query("offset") offset: Int
    ): PokemonTypeListResponseDto
}
