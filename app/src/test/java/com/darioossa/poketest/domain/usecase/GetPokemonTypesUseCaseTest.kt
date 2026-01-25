package com.darioossa.poketest.domain.usecase

import com.darioossa.poketest.data.repository.PokemonRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test
import kotlin.test.assertFailsWith

class GetPokemonTypesUseCaseTest {
    @Test
    fun `invoke returns type list from repository`() = runTest {
        val repository = mockk<PokemonRepository>()
        val expected = listOf("fire", "water")
        coEvery { repository.getPokemonTypes() } returns expected

        val useCase = GetPokemonTypesUseCase(repository)

        val result = useCase()

        assertEquals(expected, result)
    }

    @Test
    fun `invoke propagates repository errors`() = runTest {
        val repository = mockk<PokemonRepository>()
        coEvery { repository.getPokemonTypes() } throws IllegalStateException("Failure")

        val useCase = GetPokemonTypesUseCase(repository)

        assertFailsWith<IllegalStateException> {
            useCase()
        }
    }
}
