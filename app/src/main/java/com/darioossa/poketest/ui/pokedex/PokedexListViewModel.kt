package com.darioossa.poketest.ui.pokedex

import androidx.lifecycle.viewModelScope
import com.darioossa.poketest.domain.model.PokemonSummary
import com.darioossa.poketest.domain.usecase.GetPokemonListUseCase
import com.darioossa.poketest.domain.usecase.ObserveFavoritesUseCase
import com.darioossa.poketest.domain.usecase.ToggleFavoriteUseCase
import com.darioossa.poketest.ui.base.BaseMVIViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class PokedexListViewModel(
    private val getPokemonListUseCase: GetPokemonListUseCase,
    private val toggleFavoriteUseCase: ToggleFavoriteUseCase,
    private val observeFavoritesUseCase: ObserveFavoritesUseCase,
    reducer: PokedexListReducer,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : BaseMVIViewModel<PokedexListState, PokedexListEvent, PokedexListEffect>(
    initialState = PokedexListState(
        isLoading = true,
        items = emptyList(),
        errorMessage = null
    ),
    reducer = reducer
) {
    private var latestFavorites: Set<Int> = emptySet()
    private var listJob: Job? = null

    init {
        observeFavorites()
        loadPokemonList()
    }

    fun loadPokemonList(forceRefresh: Boolean = false) {
        sendEvent(PokedexListEvent.Load)
        listJob?.cancel()
        listJob = viewModelScope.launch(dispatcher) {
            runCatching { getPokemonListUseCase(limit = 20, offset = 0, forceRefresh = forceRefresh) }
                .onSuccess { items ->
                    sendEvent(PokedexListEvent.DataLoaded(updateFavorites(items)))
                }
                .onFailure { throwable ->
                    sendEvent(PokedexListEvent.LoadFailed(throwable.message ?: "No hay Pokemones :("))
                }
        }
    }

    fun retry() {
        sendEvent(PokedexListEvent.Retry)
        loadPokemonList(forceRefresh = true)
    }

    fun toggleFavorite(pokemonId: Int) {
        viewModelScope.launch(dispatcher) {
            toggleFavoriteUseCase(pokemonId)
        }
    }

    fun openDetail(pokemonId: Int) {
        sendEvent(PokedexListEvent.OpenDetail(pokemonId))
    }

    private fun observeFavorites() {
        viewModelScope.launch(dispatcher) {
            observeFavoritesUseCase().collectLatest { favorites ->
                latestFavorites = favorites
                sendEvent(PokedexListEvent.FavoriteUpdated(updateFavorites(state.value.items)))
            }
        }
    }

    private fun updateFavorites(items: List<PokemonSummary>): List<PokemonSummary> {
        return items.map { it.copy(isFavorite = latestFavorites.contains(it.id)) }
    }
}
