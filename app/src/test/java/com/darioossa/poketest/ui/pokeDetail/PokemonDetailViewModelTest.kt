package com.darioossa.poketest.ui.pokeDetail

import com.darioossa.poketest.data.repository.PokemonRepository
import com.darioossa.poketest.domain.usecase.GetPokemonDetailUseCase
import com.darioossa.poketest.testdata.PokedexTestData
import com.darioossa.poketest.testutil.MainDispatcherRule
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class PokemonDetailViewModelTest {
    @get:Rule
    val dispatcherRule = MainDispatcherRule()
    private val repository = mockk<PokemonRepository>()

    private lateinit var viewModel: PokemonDetailViewModel

    @Before
    fun setup() {
        viewModel = PokemonDetailViewModel(
            getPokemonDetailUseCase = GetPokemonDetailUseCase(repository),
            reducer = PokemonDetailReducer(),
            dispatcher = dispatcherRule.dispatcher
        )
    }

    @Test
    fun `load populates detail state`() = runTest {
        coEvery { repository.getPokemonDetail(id = 25, forceRefresh = false) } returns
                PokedexTestData.pikachuDetail

        viewModel.loadPokemonDetail(25)
        advanceUntilIdle()

        val state = viewModel.state.value
        assertEquals("pikachu", state.detail?.name)
    }

    @Test
    fun `load handles errors by showing message`() = runTest {
        coEvery { repository.getPokemonDetail(id = 42, forceRefresh = false) } throws
                IllegalStateException("Boom")

        viewModel.loadPokemonDetail(42)
        advanceUntilIdle()

        val state = viewModel.state.value
        assertNull(state.detail)
        assertEquals("Boom", state.errorMessage)
    }

    @Test
    fun `load ignores stale responses when newer request completes first`() = runTest {
        val firstDetail = PokedexTestData.pikachuDetail.copy(id = 1, name = "bulbasaur")
        val secondDetail = PokedexTestData.pikachuDetail.copy(id = 25, name = "pikachu")
        val firstDeferred = CompletableDeferred(firstDetail)
        val secondDeferred = CompletableDeferred(secondDetail)

        coEvery { repository.getPokemonDetail(id = 1, forceRefresh = false) } coAnswers
                { firstDeferred.await() }
        coEvery { repository.getPokemonDetail(id = 25, forceRefresh = false) } coAnswers
                { secondDeferred.await() }

        viewModel.loadPokemonDetail(1)
        viewModel.loadPokemonDetail(25)

        secondDeferred.complete(secondDetail)
        advanceUntilIdle()

        assertEquals("pikachu", viewModel.state.value.detail?.name)

        firstDeferred.complete(firstDetail)
        advanceUntilIdle()

        assertEquals("pikachu", viewModel.state.value.detail?.name)
    }
}
