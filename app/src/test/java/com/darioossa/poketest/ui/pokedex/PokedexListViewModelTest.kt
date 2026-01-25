package com.darioossa.poketest.ui.pokedex

import com.darioossa.poketest.data.repository.PokemonRepository
import com.darioossa.poketest.domain.model.PokemonSummary
import com.darioossa.poketest.domain.usecase.GetPokemonListUseCase
import com.darioossa.poketest.domain.usecase.GetPokemonTypesUseCase
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

    private val repository = mockk<PokemonRepository>()
    private lateinit var viewModel: PokedexListViewModel

    fun initViewModel() {
        viewModel = PokedexListViewModel(
            getPokemonListUseCase = GetPokemonListUseCase(repository),
            getPokemonTypesUseCase = GetPokemonTypesUseCase(repository),
            toggleFavoriteUseCase = ToggleFavoriteUseCase(repository),
            observeFavoritesUseCase = ObserveFavoritesUseCase(repository),
            reducer = PokedexListReducer(),
            dispatcher = dispatcherRule.dispatcher
        )
    }

    @Test
    fun `load populates list state`() = runTest(dispatcherRule.dispatcher) {
        coEvery { repository.getPokemonList(limit = 20, offset = 0, forceRefresh = false) } returns
            listOf(PokedexTestData.pikachuSummary, PokedexTestData.bulbasaurSummary)
        coEvery { repository.getPokemonTypes() } returns listOf("electric", "grass")
        every { repository.observeFavorites() } returns flowOf(emptySet())

        initViewModel()
        advanceUntilIdle()

        val state = viewModel.state.value
        assertEquals(2, state.items.size)
        assertEquals(2, state.visibleItems.size)
        assertEquals("pikachu", state.visibleItems.first().name)
    }

    @Test
    fun `favorites updates reflect in list state`() = runTest(dispatcherRule.dispatcher) {
        val favoritesFlow = MutableStateFlow(setOf<Int>())
        coEvery { repository.getPokemonList(limit = 20, offset = 0, forceRefresh = false) } returns
            listOf(PokedexTestData.pikachuSummary)
        coEvery { repository.getPokemonTypes() } returns listOf("electric")
        every { repository.observeFavorites() } returns favoritesFlow

        initViewModel()
        advanceUntilIdle()
        var state = viewModel.state.value
        assertEquals(false, state.visibleItems.first().isFavorite)

        favoritesFlow.value = setOf(25)
        advanceUntilIdle()
        state = viewModel.state.value
        assertEquals(true, state.visibleItems.first().isFavorite)
    }

    @Test
    fun `load handles errors by updating error message`() = runTest(dispatcherRule.dispatcher) {
        coEvery { repository.getPokemonList(limit = 20, offset = 0, forceRefresh = false) } throws
            IllegalStateException("Network down")
        coEvery { repository.getPokemonTypes() } returns listOf("electric")
        every { repository.observeFavorites() } returns flowOf(emptySet())

        initViewModel()
        advanceUntilIdle()

        val state = viewModel.state.value
        assertNull(state.visibleItems.firstOrNull())
        assertEquals("Network down", state.errorMessage)
    }

    @Test
    fun `search query filters visible items`() = runTest(dispatcherRule.dispatcher) {
        coEvery { repository.getPokemonList(limit = 20, offset = 0, forceRefresh = false) } returns
            listOf(PokedexTestData.pikachuSummary, PokedexTestData.bulbasaurSummary)
        coEvery { repository.getPokemonTypes() } returns listOf("electric", "grass")
        every { repository.observeFavorites() } returns flowOf(emptySet())

        initViewModel()
        advanceUntilIdle()

        viewModel.updateSearchQuery("pika")
        advanceUntilIdle()

        val state = viewModel.state.value
        assertEquals("pika", state.query)
        assertEquals(1, state.visibleItems.size)
        assertEquals("pikachu", state.visibleItems.first().name)
    }

    @Test
    fun `search query with no matches yields empty list`() = runTest(dispatcherRule.dispatcher) {
        coEvery { repository.getPokemonList(limit = 20, offset = 0, forceRefresh = false) } returns
            listOf(PokedexTestData.pikachuSummary)
        coEvery { repository.getPokemonTypes() } returns listOf("electric")
        every { repository.observeFavorites() } returns flowOf(emptySet())

        initViewModel()
        advanceUntilIdle()

        viewModel.updateSearchQuery("zzz")
        advanceUntilIdle()

        val state = viewModel.state.value
        assertEquals(0, state.visibleItems.size)
    }

    @Test
    fun `filters combine with search query`() = runTest(dispatcherRule.dispatcher) {
        val favoritesFlow = MutableStateFlow(setOf(1))
        coEvery { repository.getPokemonList(limit = 20, offset = 0, forceRefresh = false) } returns
            listOf(PokedexTestData.pikachuSummary, PokedexTestData.bulbasaurSummary)
        coEvery { repository.getPokemonTypes() } returns listOf("electric", "grass")
        every { repository.observeFavorites() } returns favoritesFlow

        initViewModel()
        advanceUntilIdle()

        viewModel.updateSearchQuery("bulb")
        viewModel.applyFilters(favoritesOnly = true, selectedTypes = setOf("grass"))
        advanceUntilIdle()

        val state = viewModel.state.value
        assertEquals(1, state.visibleItems.size)
        assertEquals("bulbasaur", state.visibleItems.first().name)
    }

    @Test
    fun `load more appends items`() = runTest(dispatcherRule.dispatcher) {
        val firstPage = makePage(1)
        val secondPage = makePage(21)
        coEvery { repository.getPokemonList(limit = 20, offset = 0, forceRefresh = false) } returns
            firstPage
        coEvery { repository.getPokemonList(limit = 20, offset = 20, forceRefresh = false) } returns
            secondPage
        coEvery { repository.getPokemonTypes() } returns listOf("electric", "grass")
        every { repository.observeFavorites() } returns flowOf(emptySet())

        initViewModel()
        advanceUntilIdle()

        viewModel.loadMore()
        advanceUntilIdle()

        val state = viewModel.state.value
        assertEquals(40, state.items.size)
        assertEquals(40, state.visibleItems.size)
        assertEquals(null, state.loadMoreError)
    }

    @Test
    fun `load more failure sets error state`() = runTest(dispatcherRule.dispatcher) {
        val firstPage = makePage(1)
        coEvery { repository.getPokemonList(limit = 20, offset = 0, forceRefresh = false) } returns
            firstPage
        coEvery { repository.getPokemonList(limit = 20, offset = 20, forceRefresh = false) } throws
            IllegalStateException("Timeout")
        coEvery { repository.getPokemonTypes() } returns listOf("electric")
        every { repository.observeFavorites() } returns flowOf(emptySet())

        initViewModel()
        advanceUntilIdle()

        viewModel.loadMore()
        advanceUntilIdle()

        val state = viewModel.state.value
        assertEquals("Timeout", state.loadMoreError)
        assertEquals(false, state.isLoadingMore)
    }

    private fun makePage(startId: Int): List<PokemonSummary> {
        return (startId until startId + 20).map { id ->
            PokedexTestData.pikachuSummary.copy(id = id, name = "pokemon$id")
        }
    }
}
