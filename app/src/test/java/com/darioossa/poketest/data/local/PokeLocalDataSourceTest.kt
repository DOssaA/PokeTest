package com.darioossa.poketest.data.local

import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class PokeLocalDataSourceTest {
    private val dao = mockk<PokemonDao>()
    private val dataSource = PokeLocalDataSourceImpl(dao)

    @Test
    fun `getPokemonList returns dao results`() = runTest {
        val expected = listOf(samplePokemonEntity())
        coEvery { dao.getPokemonList(20, 0) } returns expected

        val result = dataSource.getPokemonList(20, 0)

        assertEquals(expected, result)
        coVerify(exactly = 1) { dao.getPokemonList(20, 0) }
    }

    @Test
    fun `getPokemonWithDetails returns dao results`() = runTest {
        val expected = samplePokemonWithDetails()
        coEvery { dao.getPokemonWithDetails(25) } returns expected

        val result = dataSource.getPokemonWithDetails(25)

        assertEquals(expected, result)
        coVerify(exactly = 1) { dao.getPokemonWithDetails(25) }
    }

    @Test
    fun `savePokemonList inserts entities`() = runTest {
        val entities = listOf(samplePokemonEntity())
        coEvery { dao.insertPokemon(entities) } returns Unit

        dataSource.savePokemonList(entities)

        coVerify(exactly = 1) { dao.insertPokemon(entities) }
    }

    @Test
    fun `savePokemonDetails clears and inserts all details`() = runTest {
        val pokemon = samplePokemonEntity()
        val stats = listOf(PokemonStatEntity(pokemon.id, "hp", 35))
        val abilities = listOf(PokemonAbilityEntity(pokemon.id, "static", false))
        coEvery { dao.insertPokemon(listOf(pokemon)) } returns Unit
        coEvery { dao.clearStatsForPokemon(pokemon.id) } returns Unit
        coEvery { dao.clearAbilitiesForPokemon(pokemon.id) } returns Unit
        coEvery { dao.insertStats(stats) } returns Unit
        coEvery { dao.insertAbilities(abilities) } returns Unit

        dataSource.savePokemonDetails(pokemon, stats, abilities)

        coVerify(exactly = 1) { dao.insertPokemon(listOf(pokemon)) }
        coVerify(exactly = 1) { dao.clearStatsForPokemon(pokemon.id) }
        coVerify(exactly = 1) { dao.clearAbilitiesForPokemon(pokemon.id) }
        coVerify(exactly = 1) { dao.insertStats(stats) }
        coVerify(exactly = 1) { dao.insertAbilities(abilities) }
    }

    @Test
    fun `savePokemonDetails skips empty stats and abilities`() = runTest {
        val pokemon = samplePokemonEntity()
        coEvery { dao.insertPokemon(listOf(pokemon)) } returns Unit
        coEvery { dao.clearStatsForPokemon(pokemon.id) } returns Unit
        coEvery { dao.clearAbilitiesForPokemon(pokemon.id) } returns Unit

        dataSource.savePokemonDetails(pokemon, emptyList(), emptyList())

        coVerify(exactly = 1) { dao.insertPokemon(listOf(pokemon)) }
        coVerify(exactly = 1) { dao.clearStatsForPokemon(pokemon.id) }
        coVerify(exactly = 1) { dao.clearAbilitiesForPokemon(pokemon.id) }
        coVerify(exactly = 0) { dao.insertStats(any()) }
        coVerify(exactly = 0) { dao.insertAbilities(any()) }
    }

    private fun samplePokemonEntity() = PokemonEntity(
        id = 25,
        name = "pikachu",
        imageUrl = "https://example.com/pikachu.png",
        typesCsv = "electric",
        height = 4,
        weight = 60,
        description = "Electric mouse",
        category = "Mouse",
        lastUpdated = 1_700_000_000_000
    )

    private fun samplePokemonWithDetails() = PokemonWithDetails(
        pokemon = samplePokemonEntity(),
        stats = listOf(PokemonStatEntity(25, "hp", 35)),
        abilities = listOf(PokemonAbilityEntity(25, "static", false))
    )
}
