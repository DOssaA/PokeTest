package com.darioossa.poketest.ui.base

import androidx.lifecycle.ViewModel
import com.darioossa.poketest.domain.Reducer
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow

/**
 * Base MVI ViewModel
 * This class is a base class for all ViewModels in the app. It implements the MVI pattern.
 * 
 * @param State The state as [Reducer.ViewState]
 * @param Event The user interactions as [Reducer.ViewEvent]
 * @param Effect Actions such as Navigation or displaying a Snackbar/Toast as [Reducer.ViewEffect]
 */
open class BaseMVIViewModel<State : Reducer.ViewState, Event : Reducer.ViewEvent, Effect : Reducer.ViewEffect>(
    initialState: State,
    private val reducer: Reducer<State, Event, Effect>
) : ViewModel() {
    private val _state: MutableStateFlow<State> = MutableStateFlow(initialState)
    val state: StateFlow<State>
        get() = _state.asStateFlow()

    private val _effects = Channel<Effect>(capacity = Channel.BUFFERED)
    val effect = _effects.receiveAsFlow()

    fun sendEffect(effect: Effect) {
        _effects.trySend(effect)
    }

    fun sendEvent(event: Event) {
        val (newState, effect) = reducer.reduce(_state.value, event)

        val success = _state.tryEmit(newState)

        effect?.let {
            sendEffect(it)
        }
    }
}