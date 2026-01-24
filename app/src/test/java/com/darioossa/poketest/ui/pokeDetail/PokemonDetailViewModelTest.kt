package com.darioossa.poketest.ui.pokeDetail

import com.darioossa.poketest.data.repository.PokemonRepository
import com.darioossa.poketest.domain.usecase.GetPokemonDetailUseCase
import com.darioossa.poketest.testdata.PokedexTestData
import com.darioossa.poketest.testutil.MainDispatcherRule
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Rule
import org.junit.Test

class PokemonDetailViewModelTest {
    @get:Rule
    val dispatcherRule = MainDispatcherRule()

    @Test
    fun `load populates detail state`() = runTest {
        val repository = mockk<PokemonRepository>()
        coEvery { repository.getPokemonDetail(id = 25, forceRefresh = false) } returns PokedexTestData.pikachuDetail

        val viewModel = PokemonDetailViewModel(
            getPokemonDetailUseCase = GetPokemonDetailUseCase(repository),
            reducer = PokemonDetailReducer(),
            dispatcher = dispatcherRule.dispatcher
        )

        viewModel.loadPokemonDetail(25)
        advanceUntilIdle()

        val state = viewModel.state.value
        assertEquals("pikachu", state.detail?.name)
    }

    @Test
    fun `load handles errors by showing message`() = runTest {
        val repository = mockk<PokemonRepository>()
        coEvery { repository.getPokemonDetail(id = 42, forceRefresh = false) } throws IllegalStateException("Boom")

        val viewModel = PokemonDetailViewModel(
            getPokemonDetailUseCase = GetPokemonDetailUseCase(repository),
            reducer = PokemonDetailReducer(),
            dispatcher = dispatcherRule.dispatcher
        )

        viewModel.loadPokemonDetail(42)
        advanceUntilIdle()

        val state = viewModel.state.value
        assertNull(state.detail)
        assertEquals("Boom", state.errorMessage)
    }
}
