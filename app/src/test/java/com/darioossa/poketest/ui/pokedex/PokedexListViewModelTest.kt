package com.darioossa.poketest.ui.pokedex

import com.darioossa.poketest.data.repository.PokemonRepository
import com.darioossa.poketest.domain.usecase.GetPokemonListUseCase
import com.darioossa.poketest.domain.usecase.ObserveFavoritesUseCase
import com.darioossa.poketest.domain.usecase.ToggleFavoriteUseCase
import com.darioossa.poketest.testdata.PokedexTestData
import com.darioossa.poketest.testutil.MainDispatcherRule
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Rule
import org.junit.Test

class PokedexListViewModelTest {
    @get:Rule
    val dispatcherRule = MainDispatcherRule()

    @Test
    fun `load populates list state`() = runTest {
        val repository = mockk<PokemonRepository>()
        coEvery { repository.getPokemonList(limit = 20, offset = 0, forceRefresh = false) } returns
            listOf(PokedexTestData.pikachuSummary, PokedexTestData.bulbasaurSummary)
        every { repository.observeFavorites() } returns flowOf(emptySet())

        val viewModel = PokedexListViewModel(
            getPokemonListUseCase = GetPokemonListUseCase(repository),
            toggleFavoriteUseCase = ToggleFavoriteUseCase(repository),
            observeFavoritesUseCase = ObserveFavoritesUseCase(repository),
            reducer = PokedexListReducer(),
            ioDispatcher = dispatcherRule.dispatcher
        )

        advanceUntilIdle()

        val state = viewModel.state.value
        assertEquals(2, state.items.size)
        assertEquals("pikachu", state.items.first().name)
    }

    @Test
    fun `favorites updates reflect in list state`() = runTest {
        val repository = mockk<PokemonRepository>()
        val favoritesFlow = MutableStateFlow(setOf<Int>())
        coEvery { repository.getPokemonList(limit = 20, offset = 0, forceRefresh = false) } returns
            listOf(PokedexTestData.pikachuSummary)
        every { repository.observeFavorites() } returns favoritesFlow

        val viewModel = PokedexListViewModel(
            getPokemonListUseCase = GetPokemonListUseCase(repository),
            toggleFavoriteUseCase = ToggleFavoriteUseCase(repository),
            observeFavoritesUseCase = ObserveFavoritesUseCase(repository),
            reducer = PokedexListReducer(),
            ioDispatcher = dispatcherRule.dispatcher
        )

        advanceUntilIdle()
        var state = viewModel.state.value
        assertEquals(false, state.items.first().isFavorite)

        favoritesFlow.value = setOf(25)
        advanceUntilIdle()
        state = viewModel.state.value
        assertEquals(true, state.items.first().isFavorite)
    }

    @Test
    fun `load handles errors by updating error message`() = runTest {
        val repository = mockk<PokemonRepository>()
        coEvery { repository.getPokemonList(limit = 20, offset = 0, forceRefresh = false) } throws
            IllegalStateException("Network down")
        every { repository.observeFavorites() } returns flowOf(emptySet())

        val viewModel = PokedexListViewModel(
            getPokemonListUseCase = GetPokemonListUseCase(repository),
            toggleFavoriteUseCase = ToggleFavoriteUseCase(repository),
            observeFavoritesUseCase = ObserveFavoritesUseCase(repository),
            reducer = PokedexListReducer(),
            ioDispatcher = dispatcherRule.dispatcher
        )

        advanceUntilIdle()

        val state = viewModel.state.value
        assertNull(state.items.firstOrNull())
        assertEquals("Network down", state.errorMessage)
    }
}
