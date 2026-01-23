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
                previousState.copy(isLoading = true, errorMessage = null) to null
            }
            is PokedexListEvent.Retry -> {
                previousState.copy(isLoading = true, errorMessage = null) to null
            }
            is PokedexListEvent.DataLoaded -> {
                previousState.copy(isLoading = false, items = event.items, errorMessage = null) to null
            }
            is PokedexListEvent.LoadFailed -> {
                previousState.copy(isLoading = false, errorMessage = event.message) to null
            }
            is PokedexListEvent.FavoriteUpdated -> {
                previousState.copy(items = event.items) to null
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
    val errorMessage: String?
) : Reducer.ViewState

sealed interface PokedexListEvent : Reducer.ViewEvent {
    data object Load : PokedexListEvent
    data object Retry : PokedexListEvent
    data class DataLoaded(val items: List<PokemonSummary>) : PokedexListEvent
    data class LoadFailed(val message: String) : PokedexListEvent
    data class FavoriteUpdated(val items: List<PokemonSummary>) : PokedexListEvent
    data class OpenDetail(val pokemonId: Int) : PokedexListEvent
}

sealed interface PokedexListEffect : Reducer.ViewEffect {
    data class NavigateToDetail(val pokemonId: Int) : PokedexListEffect
}
