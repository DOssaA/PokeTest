package com.darioossa.poketest.domain

/**
 * Reducer interface
 * This interface is used to define the reducer for the MVI pattern.
 *
 * @param State The representation of the UI. Should contain everything the screen needs to display
 * @param Event The event of the view, holds all the user interactions. To be used by the ViewModel
 * to trigger state changes.
 * @param Effect The effect of the view, special kind of ViewEvent. Its role is to be fired into
 * the UI by the ViewModel. Actions such as Navigation or displaying a Snackbar/Toast.
 */
interface Reducer<State : Reducer.ViewState, Event : Reducer.ViewEvent, Effect : Reducer.ViewEffect> {
    interface ViewState

    interface ViewEvent

    interface ViewEffect

    fun reduce(previousState: State, event: Event): Pair<State, Effect?>
}