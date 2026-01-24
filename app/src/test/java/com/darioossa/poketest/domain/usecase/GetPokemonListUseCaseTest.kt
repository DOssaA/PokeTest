package com.darioossa.poketest.domain.usecase

import com.darioossa.poketest.data.repository.PokemonRepository
import com.darioossa.poketest.testdata.PokedexTestData
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import kotlin.test.assertFailsWith
import org.junit.Test

class GetPokemonListUseCaseTest {
    @Test
    fun `invoke returns list from repository`() = runTest {
        val repository = mockk<PokemonRepository>()
        val expected = listOf(PokedexTestData.pikachuSummary, PokedexTestData.bulbasaurSummary)
        coEvery { repository.getPokemonList(limit = 20, offset = 0, forceRefresh = false) } returns expected

        val useCase = GetPokemonListUseCase(repository)

        val result = useCase(limit = 20, offset = 0)

        assertEquals(expected, result)
    }

    @Test
    fun `invoke propagates repository errors`() = runTest {
        val repository = mockk<PokemonRepository>()
        coEvery { repository.getPokemonList(limit = 20, offset = 0, forceRefresh = false) } throws
            IllegalStateException("Failure")

        val useCase = GetPokemonListUseCase(repository)

        assertFailsWith<IllegalStateException> {
            useCase(limit = 20, offset = 0)
        }
    }
}
