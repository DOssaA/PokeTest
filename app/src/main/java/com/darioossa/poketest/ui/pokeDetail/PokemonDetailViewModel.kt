package com.darioossa.poketest.ui.pokeDetail

import androidx.lifecycle.viewModelScope
import com.darioossa.poketest.domain.usecase.GetPokemonDetailUseCase
import com.darioossa.poketest.ui.base.BaseMVIViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PokemonDetailViewModel(
    private val getPokemonDetailUseCase: GetPokemonDetailUseCase,
    reducer: PokemonDetailReducer,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : BaseMVIViewModel<PokemonDetailState, PokemonDetailEvent, PokemonDetailEffect>(
    initialState = PokemonDetailState(
        isLoading = true,
        detail = null,
        errorMessage = null
    ),
    reducer = reducer
) {
    fun loadPokemonDetail(id: Int, forceRefresh: Boolean = false) {
        sendEvent(PokemonDetailEvent.Load)
        viewModelScope.launch(dispatcher) {
            runCatching { getPokemonDetailUseCase(id, forceRefresh) }
                .onSuccess { sendEvent(PokemonDetailEvent.Loaded(it)) }
                .onFailure { sendEvent(PokemonDetailEvent.LoadFailed(it.message ?: "Unable to load details")) }
        }
    }
}
