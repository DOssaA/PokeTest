package com.darioossa.poketest.ui.pokedex

import com.darioossa.poketest.domain.Reducer
import com.darioossa.poketest.domain.model.PokemonDetail

class PokemonDetailReducer : Reducer<PokemonDetailState, PokemonDetailEvent, PokemonDetailEffect> {
    override fun reduce(
        previousState: PokemonDetailState,
        event: PokemonDetailEvent
    ): Pair<PokemonDetailState, PokemonDetailEffect?> {
        return when (event) {
            is PokemonDetailEvent.Load -> {
                previousState.copy(isLoading = true, errorMessage = null) to null
            }
            is PokemonDetailEvent.Loaded -> {
                previousState.copy(isLoading = false, detail = event.detail, errorMessage = null) to null
            }
            is PokemonDetailEvent.LoadFailed -> {
                previousState.copy(isLoading = false, errorMessage = event.message) to null
            }
        }
    }
}

data class PokemonDetailState(
    val isLoading: Boolean,
    val detail: PokemonDetail?,
    val errorMessage: String?
) : Reducer.ViewState

sealed interface PokemonDetailEvent : Reducer.ViewEvent {
    data object Load : PokemonDetailEvent
    data class Loaded(val detail: PokemonDetail) : PokemonDetailEvent
    data class LoadFailed(val message: String) : PokemonDetailEvent
}

sealed interface PokemonDetailEffect : Reducer.ViewEffect
