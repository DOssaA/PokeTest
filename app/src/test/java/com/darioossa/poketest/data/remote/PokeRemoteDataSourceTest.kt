package com.darioossa.poketest.data.remote

import com.darioossa.poketest.data.remote.dto.NamedApiResourceDto
import com.darioossa.poketest.data.remote.dto.PokemonDetailDto
import com.darioossa.poketest.data.remote.dto.PokemonListResponseDto
import com.darioossa.poketest.data.remote.dto.PokemonSpeciesDto
import com.darioossa.poketest.data.remote.dto.PokemonSpritesDto
import com.darioossa.poketest.data.remote.dto.PokemonStatSlotDto
import com.darioossa.poketest.data.remote.dto.PokemonTypeSlotDto
import com.darioossa.poketest.data.remote.dto.PokemonAbilitySlotDto
import com.darioossa.poketest.data.remote.dto.PokemonOtherSpritesDto
import com.darioossa.poketest.data.remote.dto.PokemonOfficialArtworkDto
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class PokeRemoteDataSourceTest {
    private val service = mockk<PokeApiService>()
    private val dataSource = PokeRemoteDataSourceImpl(service)

    @Test
    fun `fetchPokemonList delegates to service`() = runTest {
        val response = PokemonListResponseDto(
            count = 1,
            next = null,
            previous = null,
            results = listOf(NamedApiResourceDto("pikachu", "https://pokeapi.co/api/v2/pokemon/25/"))
        )
        coEvery { service.getPokemonList(20, 0) } returns response

        val result = dataSource.fetchPokemonList(20, 0)

        assertEquals(response, result)
        coVerify(exactly = 1) { service.getPokemonList(20, 0) }
    }

    @Test
    fun `fetchPokemonDetail delegates to service`() = runTest {
        val detail = sampleDetail()
        coEvery { service.getPokemonDetail("25") } returns detail

        val result = dataSource.fetchPokemonDetail("25")

        assertEquals(detail, result)
        coVerify(exactly = 1) { service.getPokemonDetail("25") }
    }

    @Test
    fun `fetchPokemonSpecies delegates to service`() = runTest {
        val species = PokemonSpeciesDto(
            flavorTextEntries = emptyList(),
            genera = emptyList()
        )
        coEvery { service.getPokemonSpecies("25") } returns species

        val result = dataSource.fetchPokemonSpecies("25")

        assertEquals(species, result)
        coVerify(exactly = 1) { service.getPokemonSpecies("25") }
    }

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
}
