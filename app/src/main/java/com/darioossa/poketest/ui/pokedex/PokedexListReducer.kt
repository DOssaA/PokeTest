package com.darioossa.poketest.ui.pokedex

import com.darioossa.poketest.domain.Reducer
import com.darioossa.poketest.domain.model.PokemonSummary

class PokedexListReducer : Reducer<PokedexListState, PokedexListEvent, PokedexListEffect> {
    override fun reduce(
        previousState: PokedexListState,
        event: PokedexListEvent
    ): Pair<PokedexListState, PokedexListEffect?> {
        return when (event) {
            is PokedexListEvent.Load -> {
                previousState.copy(
                    isLoading = true,
                    errorMessage = null,
                    loadMoreError = null
                ) to null
            }
            is PokedexListEvent.Retry -> {
                previousState.copy(
                    isLoading = true,
                    errorMessage = null,
                    loadMoreError = null
                ) to null
            }
            is PokedexListEvent.DataLoaded -> {
                val visibleItems = applyFilters(
                    items = event.items,
                    query = previousState.query,
                    favoritesOnly = previousState.favoritesOnly,
                    selectedTypes = previousState.selectedTypes
                )
                previousState.copy(
                    isLoading = false,
                    items = event.items,
                    visibleItems = visibleItems,
                    errorMessage = null
                ) to null
            }
            is PokedexListEvent.LoadFailed -> {
                previousState.copy(isLoading = false, errorMessage = event.message) to null
            }
            is PokedexListEvent.FavoriteUpdated -> {
                val visibleItems = applyFilters(
                    items = event.items,
                    query = previousState.query,
                    favoritesOnly = previousState.favoritesOnly,
                    selectedTypes = previousState.selectedTypes
                )
                previousState.copy(items = event.items, visibleItems = visibleItems) to null
            }
            is PokedexListEvent.SearchQueryChanged -> {
                val visibleItems = applyFilters(
                    items = previousState.items,
                    query = event.query,
                    favoritesOnly = previousState.favoritesOnly,
                    selectedTypes = previousState.selectedTypes
                )
                previousState.copy(query = event.query, visibleItems = visibleItems) to null
            }
            is PokedexListEvent.FiltersChanged -> {
                val visibleItems = applyFilters(
                    items = previousState.items,
                    query = previousState.query,
                    favoritesOnly = event.favoritesOnly,
                    selectedTypes = event.selectedTypes
                )
                previousState.copy(
                    favoritesOnly = event.favoritesOnly,
                    selectedTypes = event.selectedTypes,
                    visibleItems = visibleItems
                ) to null
            }
            is PokedexListEvent.TypesLoaded -> {
                previousState.copy(availableTypes = event.types) to null
            }
            is PokedexListEvent.LoadMoreStarted -> {
                previousState.copy(isLoadingMore = true, loadMoreError = null) to null
            }
            is PokedexListEvent.LoadMoreFailed -> {
                previousState.copy(isLoadingMore = false, loadMoreError = event.message) to null
            }
            is PokedexListEvent.MoreDataLoaded -> {
                val updatedItems = previousState.items + event.items
                val visibleItems = applyFilters(
                    items = updatedItems,
                    query = previousState.query,
                    favoritesOnly = previousState.favoritesOnly,
                    selectedTypes = previousState.selectedTypes
                )
                previousState.copy(
                    items = updatedItems,
                    visibleItems = visibleItems,
                    isLoadingMore = false,
                    loadMoreError = null
                ) to null
            }
            is PokedexListEvent.OpenDetail -> {
                previousState to PokedexListEffect.NavigateToDetail(event.pokemonId)
            }
        }
    }
}

data class PokedexListState(
    val isLoading: Boolean,
    val items: List<PokemonSummary>,
    val visibleItems: List<PokemonSummary>,
    val query: String,
    val favoritesOnly: Boolean,
    val selectedTypes: Set<String>,
    val availableTypes: List<String>,
    val isLoadingMore: Boolean,
    val loadMoreError: String?,
    val errorMessage: String?
) : Reducer.ViewState

sealed interface PokedexListEvent : Reducer.ViewEvent {
    data object Load : PokedexListEvent
    data object Retry : PokedexListEvent
    data class DataLoaded(val items: List<PokemonSummary>) : PokedexListEvent
    data class LoadFailed(val message: String) : PokedexListEvent
    data class FavoriteUpdated(val items: List<PokemonSummary>) : PokedexListEvent
    data class SearchQueryChanged(val query: String) : PokedexListEvent
    data class FiltersChanged(val favoritesOnly: Boolean, val selectedTypes: Set<String>) : PokedexListEvent
    data class TypesLoaded(val types: List<String>) : PokedexListEvent
    data object LoadMoreStarted : PokedexListEvent
    data class MoreDataLoaded(val items: List<PokemonSummary>) : PokedexListEvent
    data class LoadMoreFailed(val message: String) : PokedexListEvent
    data class OpenDetail(val pokemonId: Int) : PokedexListEvent
}

sealed interface PokedexListEffect : Reducer.ViewEffect {
    data class NavigateToDetail(val pokemonId: Int) : PokedexListEffect
}

private fun applyFilters(
    items: List<PokemonSummary>,
    query: String,
    favoritesOnly: Boolean,
    selectedTypes: Set<String>
): List<PokemonSummary> {
    var filtered = items
    if (favoritesOnly) {
        filtered = filtered.filter { it.isFavorite }
    }
    if (selectedTypes.isNotEmpty()) {
        filtered = filtered.filter { pokemon ->
            pokemon.types.any { type -> selectedTypes.contains(type) }
        }
    }
    if (query.isNotBlank()) {
        filtered = filtered.filter { it.name.contains(query, ignoreCase = true) }
    }
    return filtered
}
