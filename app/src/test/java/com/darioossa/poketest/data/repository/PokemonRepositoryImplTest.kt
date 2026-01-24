package com.darioossa.poketest.data.repository

import com.darioossa.poketest.data.local.FavoritesDataStore
import com.darioossa.poketest.data.local.PokeLocalDataSource
import com.darioossa.poketest.data.local.PokemonAbilityEntity
import com.darioossa.poketest.data.local.PokemonEntity
import com.darioossa.poketest.data.local.PokemonStatEntity
import com.darioossa.poketest.data.local.PokemonWithDetails
import com.darioossa.poketest.data.remote.PokeRemoteDataSource
import com.darioossa.poketest.data.remote.dto.NamedApiResourceDto
import com.darioossa.poketest.data.remote.dto.PokemonAbilitySlotDto
import com.darioossa.poketest.data.remote.dto.PokemonDetailDto
import com.darioossa.poketest.data.remote.dto.PokemonFlavorTextDto
import com.darioossa.poketest.data.remote.dto.PokemonGenusDto
import com.darioossa.poketest.data.remote.dto.PokemonListResponseDto
import com.darioossa.poketest.data.remote.dto.PokemonOfficialArtworkDto
import com.darioossa.poketest.data.remote.dto.PokemonOtherSpritesDto
import com.darioossa.poketest.data.remote.dto.PokemonSpeciesDto
import com.darioossa.poketest.data.remote.dto.PokemonSpritesDto
import com.darioossa.poketest.data.remote.dto.PokemonStatSlotDto
import com.darioossa.poketest.data.remote.dto.PokemonTypeSlotDto
import io.mockk.every
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import kotlin.test.assertFailsWith

class PokemonRepositoryImplTest {
    private val remote = mockk<PokeRemoteDataSource>()
    private val local = mockk<PokeLocalDataSource>()
    private val favoritesStore = mockk<FavoritesDataStore>()

    private val repository = PokemonRepositoryImpl(
        remote = remote,
        local = local,
        favoritesStore = favoritesStore
    )

    @Test
    fun `getPokemonList returns cached data when fresh`() = runTest {
        val now = System.currentTimeMillis()
        val cached = listOf(sampleEntity(now))
        coEvery { favoritesStore.getFavorites() } returns setOf(25)
        coEvery { local.getPokemonList(limit = 20, offset = 0) } returns cached

        val result = repository.getPokemonList(limit = 20, offset = 0, forceRefresh = false)

        assertEquals(1, result.size)
        assertTrue(result.first().isFavorite)
        coVerify(exactly = 0) { remote.fetchPokemonList(any(), any()) }
    }

    @Test
    fun `getPokemonList refreshes when cache is stale`() = runTest {
        val stale = listOf(sampleEntity(System.currentTimeMillis() - 25 * 60 * 60 * 1000L))
        val response = PokemonListResponseDto(
            count = 1,
            next = null,
            previous = null,
            results = listOf(NamedApiResourceDto("bulbasaur", "https://pokeapi.co/api/v2/pokemon/1/"))
        )
        coEvery { favoritesStore.getFavorites() } returns emptySet()
        coEvery { local.getPokemonList(limit = 20, offset = 0) } returns stale
        coEvery { remote.fetchPokemonList(20, 0) } returns response
        coEvery { local.savePokemonList(any()) } returns Unit

        val result = repository.getPokemonList(limit = 20, offset = 0, forceRefresh = false)

        assertEquals(1, result.size)
        assertEquals(1, result.first().id)
        coVerify(exactly = 1) { remote.fetchPokemonList(20, 0) }
        coVerify(exactly = 1) { local.savePokemonList(match { it.first().id == 1 }) }
    }

    @Test
    fun `getPokemonDetail returns cached detail when fresh`() = runTest {
        val now = System.currentTimeMillis()
        val cached = samplePokemonWithDetails(now)
        coEvery { local.getPokemonWithDetails(25) } returns cached

        val result = repository.getPokemonDetail(id = 25, forceRefresh = false)

        assertEquals("pikachu", result.name)
        coVerify(exactly = 0) { remote.fetchPokemonDetail(any()) }
        coVerify(exactly = 0) { remote.fetchPokemonSpecies(any()) }
    }

    @Test
    fun `getPokemonDetail refreshes when cache missing`() = runTest {
        val detail = sampleDetail()
        val species = sampleSpecies()
        val refreshed = samplePokemonWithDetails(System.currentTimeMillis())
        coEvery { local.getPokemonWithDetails(25) } returnsMany listOf(null, refreshed)
        coEvery { remote.fetchPokemonDetail("25") } returns detail
        coEvery { remote.fetchPokemonSpecies("25") } returns species
        coEvery { local.savePokemonDetails(any(), any(), any()) } returns Unit

        val result = repository.getPokemonDetail(id = 25, forceRefresh = false)

        assertEquals("pikachu", result.name)
        coVerify(exactly = 1) { local.savePokemonDetails(any(), any(), any()) }
    }

    @Test
    fun `getPokemonDetail propagates remote errors`() = runTest {
        coEvery { local.getPokemonWithDetails(25) } returns null
        coEvery { remote.fetchPokemonDetail("25") } throws IllegalStateException("timeout")

        assertFailsWith<IllegalStateException> {
            repository.getPokemonDetail(id = 25, forceRefresh = false)
        }
    }

    @Test
    fun `toggleFavorite returns updated favorite state`() = runTest {
        coEvery { favoritesStore.toggleFavorite(25) } returns setOf(25)

        val result = repository.toggleFavorite(25)

        assertTrue(result)
    }

    @Test
    fun `observeFavorites exposes favorites flow`() {
        val flow = MutableStateFlow(setOf(1, 2))
        every { favoritesStore.favoritesFlow } returns flow

        val result = repository.observeFavorites()

        assertEquals(flow, result)
    }

    private fun sampleEntity(timestamp: Long) = PokemonEntity(
        id = 25,
        name = "pikachu",
        imageUrl = "https://example.com/pikachu.png",
        typesCsv = "electric",
        height = 4,
        weight = 60,
        description = "Electric mouse",
        category = "Mouse",
        lastUpdated = timestamp
    )

    private fun samplePokemonWithDetails(timestamp: Long) = PokemonWithDetails(
        pokemon = sampleEntity(timestamp),
        stats = listOf(PokemonStatEntity(25, "hp", 35)),
        abilities = listOf(PokemonAbilityEntity(25, "static", false))
    )

    private fun sampleDetail() = PokemonDetailDto(
        id = 25,
        name = "pikachu",
        height = 4,
        weight = 60,
        types = listOf(PokemonTypeSlotDto(NamedApiResourceDto("electric", "url"))),
        stats = listOf(PokemonStatSlotDto(35, NamedApiResourceDto("hp", "url"))),
        abilities = listOf(PokemonAbilitySlotDto(false, NamedApiResourceDto("static", "url"))),
        sprites = PokemonSpritesDto(
            frontDefault = "front.png",
            other = PokemonOtherSpritesDto(
                officialArtwork = PokemonOfficialArtworkDto(frontDefault = "official.png")
            )
        )
    )

    private fun sampleSpecies() = PokemonSpeciesDto(
        flavorTextEntries = listOf(
            PokemonFlavorTextDto("Electric mouse.", NamedApiResourceDto("en", "url"))
        ),
        genera = listOf(
            PokemonGenusDto("Mouse Pokemon", NamedApiResourceDto("en", "url"))
        )
    )
}
