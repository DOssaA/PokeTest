package com.darioossa.poketest.domain.usecase

import com.darioossa.poketest.data.repository.PokemonRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertTrue
import org.junit.Test

class ToggleFavoriteUseCaseTest {
    @Test
    fun `invoke toggles favorite`() = runTest {
        val repository = mockk<PokemonRepository>()
        val id = 25
        coEvery { repository.toggleFavorite(id) } returns true

        val useCase = ToggleFavoriteUseCase(repository)

        val result = useCase(id)

        assertTrue(result)
    }
}
