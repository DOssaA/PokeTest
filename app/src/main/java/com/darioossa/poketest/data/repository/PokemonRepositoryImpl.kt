package com.darioossa.poketest.data.repository

import com.darioossa.poketest.data.local.FavoritesDataStore
import com.darioossa.poketest.data.local.PokeLocalDataSource
import com.darioossa.poketest.data.local.PokemonEntity
import com.darioossa.poketest.data.local.PokemonWithDetails
import com.darioossa.poketest.data.mapper.toAbilityEntities
import com.darioossa.poketest.data.mapper.toDetail
import com.darioossa.poketest.data.mapper.toPokemonEntity
import com.darioossa.poketest.data.mapper.toStatEntities
import com.darioossa.poketest.data.mapper.toSummary
import com.darioossa.poketest.data.remote.PokeRemoteDataSource
import com.darioossa.poketest.domain.model.PokemonDetail
import com.darioossa.poketest.domain.model.PokemonSummary
import kotlinx.coroutines.flow.Flow

class PokemonRepositoryImpl(
    private val remote: PokeRemoteDataSource,
    private val local: PokeLocalDataSource,
    private val favoritesStore: FavoritesDataStore
) : PokemonRepository {
    override suspend fun getPokemonList(
        limit: Int,
        offset: Int,
        forceRefresh: Boolean
    ): List<PokemonSummary> {
        val favorites = favoritesStore.getFavorites()
        val cached = local.getPokemonList(limit, offset)
        if (!forceRefresh && cached.isNotEmpty() && cached.none { isStale(it.lastUpdated) }) {
            return cached.map { it.toSummary(favorites) }
        }

        val response = remote.fetchPokemonList(limit, offset)
        val now = System.currentTimeMillis()
        val entities = response.results
            .map { it.toPokemonEntity(now) }
            .filter { it.id > 0 }
        val hydrated = hydrateTypes(entities)
        local.savePokemonList(hydrated)
        return hydrated.map { it.toSummary(favorites) }
    }

    override suspend fun getPokemonTypes(limit: Int, offset: Int): List<String> {
        return remote.fetchPokemonTypes(limit, offset)
            .results
            .map { it.name }
    }

    override suspend fun getPokemonDetail(id: Int, forceRefresh: Boolean): PokemonDetail {
        val cached = local.getPokemonWithDetails(id)
        val canUseCached = !forceRefresh && cached != null && !isStale(cached.pokemon.lastUpdated)
                && cached.hasDetails
        if (canUseCached) {
            return cached.toDetail()
        }

        val detail = remote.fetchPokemonDetail(id.toString())
        val species = remote.fetchPokemonSpecies(id.toString())
        val now = System.currentTimeMillis()
        val entity = detail.toPokemonEntity(species, now)
        local.savePokemonDetails(entity, detail.toStatEntities(), detail.toAbilityEntities())
        return local.getPokemonWithDetails(id)?.toDetail()
            ?: throw IllegalStateException("Pokemon detail not available after refresh")
    }

    override suspend fun toggleFavorite(pokemonId: Int): Boolean {
        val favorites = favoritesStore.toggleFavorite(pokemonId)
        return favorites.contains(pokemonId)
    }

    override fun observeFavorites(): Flow<Set<Int>> {
        return favoritesStore.favoritesFlow
    }

    private fun isStale(lastUpdated: Long): Boolean {
        return System.currentTimeMillis() - lastUpdated > CACHE_DURATION_MS
    }

    private suspend fun hydrateTypes(entities: List<PokemonEntity>): List<PokemonEntity> {
        return entities.map { entity ->
            val detail = remote.fetchPokemonDetail(entity.id.toString())
            entity.copy(typesCsv = detail.types.joinToString(",") { it.type.name })
        }
    }

    private val PokemonWithDetails.hasDetails get() =
        pokemon.height != null || pokemon.weight != null

    private companion object {
        private const val CACHE_DURATION_MS = 24 * 60 * 60 * 1000L // 1 day
    }
}
