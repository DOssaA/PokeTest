package com.darioossa.poketest.ui.pokedex

import androidx.lifecycle.viewModelScope
import com.darioossa.poketest.domain.model.PokemonSummary
import com.darioossa.poketest.domain.usecase.GetPokemonListUseCase
import com.darioossa.poketest.domain.usecase.GetPokemonTypesUseCase
import com.darioossa.poketest.domain.usecase.ObserveFavoritesUseCase
import com.darioossa.poketest.domain.usecase.ToggleFavoriteUseCase
import com.darioossa.poketest.ui.base.BaseMVIViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PokedexListViewModel(
    private val getPokemonListUseCase: GetPokemonListUseCase,
    private val getPokemonTypesUseCase: GetPokemonTypesUseCase,
    private val toggleFavoriteUseCase: ToggleFavoriteUseCase,
    private val observeFavoritesUseCase: ObserveFavoritesUseCase,
    reducer: PokedexListReducer,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO,
    private val mainDispatcher: CoroutineDispatcher = Dispatchers.Main
) : BaseMVIViewModel<PokedexListState, PokedexListEvent, PokedexListEffect>(
    initialState = PokedexListState(
        isLoading = true,
        items = emptyList(),
        visibleItems = emptyList(),
        query = "",
        favoritesOnly = false,
        selectedTypes = emptySet(),
        availableTypes = emptyList(),
        isLoadingMore = false,
        loadMoreError = null,
        errorMessage = null
    ),
    reducer = reducer
) {
    private var latestFavorites: Set<Int> = emptySet()
    private var favoritesLoaded: Boolean = false
    private var listJob: Job? = null
    private var nextOffset: Int = 0
    private var hasMore: Boolean = true
    private val pageSize: Int = 20

    init {
        observeFavorites()
        loadTypes()
        loadPokemonList()
    }

    fun loadPokemonList(forceRefresh: Boolean = false) {
        launchEventSafely(PokedexListEvent.Load)
        listJob?.cancel()
        listJob = viewModelScope.launch(dispatcher) {
            runCatching { getPokemonListUseCase(limit = pageSize, offset = 0, forceRefresh = forceRefresh) }
                .onSuccess { items ->
                    nextOffset = pageSize
                    hasMore = items.size == pageSize
                    sendEventSafely(PokedexListEvent.DataLoaded(updateFavorites(items)))
                }
                .onFailure { throwable ->
                    sendEventSafely(PokedexListEvent.LoadFailed(throwable.message ?: "No hay Pokemones :("))
                }
        }
    }

    fun retry() {
        launchEventSafely(PokedexListEvent.Retry)
        loadPokemonList(forceRefresh = true)
    }

    fun toggleFavorite(pokemonId: Int) {
        viewModelScope.launch(dispatcher) {
            toggleFavoriteUseCase(pokemonId)
        }
    }

    fun openDetail(pokemonId: Int) {
        launchEventSafely(PokedexListEvent.OpenDetail(pokemonId))
    }

    fun updateSearchQuery(query: String) {
        launchEventSafely(PokedexListEvent.SearchQueryChanged(query))
    }

    fun applyFilters(favoritesOnly: Boolean, selectedTypes: Set<String>) {
        launchEventSafely(PokedexListEvent.FiltersChanged(favoritesOnly, selectedTypes))
    }

    fun loadMore() {
        if (state.value.isLoading || state.value.isLoadingMore || !hasMore) return
        launchEventSafely(PokedexListEvent.LoadMoreStarted)
        viewModelScope.launch(dispatcher) {
            runCatching { getPokemonListUseCase(limit = pageSize, offset = nextOffset, forceRefresh = false) }
                .onSuccess { items ->
                    hasMore = items.size == pageSize
                    nextOffset += pageSize
                    sendEventSafely(PokedexListEvent.MoreDataLoaded(updateFavorites(items)))
                }
                .onFailure { throwable ->
                    sendEventSafely(
                        PokedexListEvent.LoadMoreFailed(
                            throwable.message ?: "No hay mas Pokemones"
                        )
                    )
                }
        }
    }

    private fun observeFavorites() {
        viewModelScope.launch(dispatcher) {
            observeFavoritesUseCase().collectLatest { favorites ->
                latestFavorites = favorites
                favoritesLoaded = true
                if (state.value.items.isNotEmpty()) {
                    sendEventSafely(PokedexListEvent.FavoriteUpdated(updateFavorites(state.value.items)))
                }
            }
        }
    }

    private fun loadTypes() {
        viewModelScope.launch(dispatcher) {
            runCatching { getPokemonTypesUseCase() }
                .onSuccess { types ->
                    sendEventSafely(PokedexListEvent.TypesLoaded(types))
                }
        }
    }

    private fun updateFavorites(items: List<PokemonSummary>): List<PokemonSummary> {
        if (!favoritesLoaded) {
            return items
        }
        return items.map { it.copy(isFavorite = latestFavorites.contains(it.id)) }
    }

    private suspend fun sendEventSafely(event: PokedexListEvent) {
        withContext(mainDispatcher) {
            sendEvent(event)
        }
    }

    private fun launchEventSafely(event: PokedexListEvent) {
        viewModelScope.launch(mainDispatcher) {
            sendEvent(event)
        }
    }
}
