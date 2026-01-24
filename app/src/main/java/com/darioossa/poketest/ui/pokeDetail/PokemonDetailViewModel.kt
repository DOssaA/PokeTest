package com.darioossa.poketest.ui.pokeDetail

import androidx.lifecycle.viewModelScope
import com.darioossa.poketest.domain.usecase.GetPokemonDetailUseCase
import com.darioossa.poketest.ui.base.BaseMVIViewModel
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
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
    private var loadJob: Job? = null
    private var currentPokemonId: Int? = null

    fun loadPokemonDetail(id: Int, forceRefresh: Boolean = false) {
        currentPokemonId = id
        loadJob?.cancel()
        sendEvent(PokemonDetailEvent.Load)
        loadJob = viewModelScope.launch(dispatcher) {
            val requestId = id
            runCatching { getPokemonDetailUseCase(id, forceRefresh) }
                .onSuccess { detail ->
                    if (currentPokemonId == requestId) {
                        sendEvent(PokemonDetailEvent.Loaded(detail))
                    }
                }
                .onFailure { throwable ->
                    if (throwable is CancellationException) return@launch
                    if (currentPokemonId == requestId) {
                        sendEvent(
                            PokemonDetailEvent.LoadFailed(
                                throwable.message ?: "Unable to load details"
                            )
                        )
                    }
                }
        }
    }
}
