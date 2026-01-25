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
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
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
        // Cache policy:
        // 1) Use cached list when fresh, but backfill missing types for filter accuracy.
        // 2) Otherwise fetch list from network, hydrate types via detail calls, then persist.
        val favorites = favoritesStore.getFavorites()
        val cached = local.getPokemonList(limit, offset)
        val cachedMissingTypes = cached.any { it.typesCsv.isNullOrBlank() }
        if (!forceRefresh && cached.isNotEmpty() && cached.none { isStale(it.lastUpdated) }) {
            val hydrated = if (cachedMissingTypes) hydrateTypes(cached) else cached
            if (cachedMissingTypes) {
                local.savePokemonList(hydrated)
            }
            return hydrated.map { it.toSummary(favorites) }
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
        // Hydration policy: best-effort detail fetch per entity with bounded concurrency.
        // Failures keep existing entity so list load doesn't fail due to one item.
        if (entities.isEmpty()) return entities
        return coroutineScope {
            entities.chunked(TYPE_HYDRATION_CONCURRENCY).flatMap { chunk ->
                chunk.map { entity ->
                    async {
                        if (!entity.typesCsv.isNullOrBlank()) {
                            entity
                        } else {
                            runCatching { remote.fetchPokemonDetail(entity.id.toString()) }
                                .map { detail ->
                                    entity.copy(
                                        typesCsv = detail.types.joinToString(",") { it.type.name }
                                    )
                                }
                                .getOrElse { entity }
                        }
                    }
                }.awaitAll()
            }
        }
    }

    private val PokemonWithDetails.hasDetails get() =
        pokemon.height != null || pokemon.weight != null

    private companion object {
        private const val CACHE_DURATION_MS = 24 * 60 * 60 * 1000L // 1 day
        private const val TYPE_HYDRATION_CONCURRENCY = 6
    }
}
