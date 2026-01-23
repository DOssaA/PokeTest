package com.darioossa.poketest.ui.base

import app.cash.turbine.test
import com.darioossa.poketest.domain.Reducer
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertSame
import org.junit.Before
import org.junit.Test
import kotlin.test.assertIs

@OptIn(ExperimentalCoroutinesApi::class)
class BaseViewModelTest {
    private val dispatcher: CoroutineDispatcher = StandardTestDispatcher()
    private val state = object : Reducer.ViewState {}

    private lateinit var viewModel: BaseMVIViewModel<Reducer.ViewState, Reducer.ViewEvent, Reducer.ViewEffect>

    private val reducer = mockk<Reducer<Reducer.ViewState, Reducer.ViewEvent, Reducer.ViewEffect>>(
        relaxed = true
    )

    @Before
    fun setup() {
        Dispatchers.setMain(dispatcher)
        viewModel = BaseMVIViewModel(state, reducer)
    }

    @Test
    fun `uses the state provided`() {
        assertIs<Reducer.ViewState>(viewModel.state.value)
        assertSame(state, viewModel.state.value)
    }

    @Test
    fun `sends effects when received`() = runBlocking {
        val effect = object: Reducer.ViewEffect {}
        viewModel.sendEffect(effect)
        assertSame(effect, viewModel.effect.first())
    }

    @Test
    fun `uses the reducer and state provided on events`() = runBlocking {
        val expectedState = object : Reducer.ViewState { val value = "" }
        val event = object: Reducer.ViewEvent {}
        testSendEventResult(event, expectedState, null)
    }

    private suspend fun testSendEventResult(
        event: Reducer.ViewEvent,
        expectedState: Reducer.ViewState,
        effect: Reducer.ViewEffect? = null
    ) {
        val prevState = viewModel.state.value
        every { reducer.reduce(any(), event) } returns Pair(expectedState, effect)
        viewModel.sendEvent(event)
        verify { reducer.reduce(prevState, event) }
        assertSame(expectedState, viewModel.state.value)
        effect?.let {
            viewModel.effect.test {
                assertSame(effect, awaitItem())
                cancelAndIgnoreRemainingEvents()
            }
        }
    }

    @Test
    fun `uses the reducer state and effect provided on events related to effects`() =
        runTest {
            val event = object: Reducer.ViewEvent {}
            val expectedState = object : Reducer.ViewState { val value = "" }
            val effect = object : Reducer.ViewEffect {}
            testSendEventResult(event, expectedState, effect)
        }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }
}