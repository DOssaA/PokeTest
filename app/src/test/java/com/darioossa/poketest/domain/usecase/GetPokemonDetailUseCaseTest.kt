package com.darioossa.poketest.domain.usecase

import com.darioossa.poketest.data.repository.PokemonRepository
import com.darioossa.poketest.testdata.PokedexTestData
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class GetPokemonDetailUseCaseTest {
    @Test
    fun `invoke returns detail from repository`() = runTest {
        val repository = mockk<PokemonRepository>()
        coEvery { repository.getPokemonDetail(id = 25, forceRefresh = false) } returns PokedexTestData.pikachuDetail

        val useCase = GetPokemonDetailUseCase(repository)

        val result = useCase(id = 25)

        assertEquals("pikachu", result.name)
        assertEquals(2, result.stats.size)
    }
}
