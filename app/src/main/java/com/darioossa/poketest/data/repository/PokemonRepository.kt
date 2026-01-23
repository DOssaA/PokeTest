package com.darioossa.poketest.data.repository

import com.darioossa.poketest.domain.model.PokemonDetail
import com.darioossa.poketest.domain.model.PokemonSummary
import kotlinx.coroutines.flow.Flow

interface PokemonRepository {
    /**
     * Returns a paged list of Pokemon summaries. Uses local cache when fresh unless
     * [forceRefresh] is true.
     */
    suspend fun getPokemonList(limit: Int, offset: Int, forceRefresh: Boolean = false): List<PokemonSummary>

    /**
     * Returns Pokemon detail data. Uses local cache when fresh unless [forceRefresh] is true.
     */
    suspend fun getPokemonDetail(id: Int, forceRefresh: Boolean = false): PokemonDetail

    /**
     * Toggles favorite state for the given Pokemon and returns the new favorite state.
     */
    suspend fun toggleFavorite(pokemonId: Int): Boolean

    /**
     * Emits the current set of favorited Pokemon IDs.
     */
    fun observeFavorites(): Flow<Set<Int>>
}
