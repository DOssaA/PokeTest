package com.darioossa.poketest.data.mapper

import com.darioossa.poketest.data.local.PokemonAbilityEntity
import com.darioossa.poketest.data.local.PokemonEntity
import com.darioossa.poketest.data.local.PokemonStatEntity
import com.darioossa.poketest.data.local.PokemonWithDetails
import com.darioossa.poketest.data.remote.dto.NamedApiResourceDto
import com.darioossa.poketest.data.remote.dto.PokemonDetailDto
import com.darioossa.poketest.data.remote.dto.PokemonSpeciesDto
import com.darioossa.poketest.domain.model.PokemonAbility
import com.darioossa.poketest.domain.model.PokemonDetail
import com.darioossa.poketest.domain.model.PokemonStat
import com.darioossa.poketest.domain.model.PokemonSummary

private const val IMAGE_BASE_URL = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork"

fun NamedApiResourceDto.toPokemonEntity(now: Long): PokemonEntity {
    val id = extractIdFromUrl(url)
    return PokemonEntity(
        id = id,
        name = name,
        imageUrl = "$IMAGE_BASE_URL/$id.png",
        typesCsv = null,
        height = null,
        weight = null,
        description = null,
        category = null,
        lastUpdated = now
    )
}

fun PokemonDetailDto.toPokemonEntity(species: PokemonSpeciesDto, now: Long): PokemonEntity {
    val description = species.englishFlavorText()
    val category = species.englishGenus()
    val image = sprites.other?.officialArtwork?.frontDefault
        ?: sprites.frontDefault
        ?: "$IMAGE_BASE_URL/$id.png"
    val typesCsv = types.joinToString(",") { it.type.name }
    return PokemonEntity(
        id = id,
        name = name,
        imageUrl = image,
        typesCsv = typesCsv,
        height = height,
        weight = weight,
        description = description,
        category = category,
        lastUpdated = now
    )
}

fun PokemonDetailDto.toStatEntities(): List<PokemonStatEntity> {
    return stats.map { stat ->
        PokemonStatEntity(
            pokemonId = id,
            name = stat.stat.name,
            value = stat.baseStat
        )
    }
}

fun PokemonDetailDto.toAbilityEntities(): List<PokemonAbilityEntity> {
    return abilities.map { ability ->
        PokemonAbilityEntity(
            pokemonId = id,
            name = ability.ability.name,
            isHidden = ability.isHidden
        )
    }
}

fun PokemonEntity.toSummary(favorites: Set<Int>): PokemonSummary {
    return PokemonSummary(
        id = id,
        name = name,
        imageUrl = imageUrl,
        types = typesCsv?.split(',')?.filter { it.isNotBlank() }.orEmpty(),
        isFavorite = favorites.contains(id)
    )
}

fun PokemonWithDetails.toDetail(): PokemonDetail {
    return PokemonDetail(
        id = pokemon.id,
        name = pokemon.name,
        imageUrl = pokemon.imageUrl,
        description = pokemon.description,
        stats = stats.map { PokemonStat(it.name, it.value) },
        abilities = abilities.map { PokemonAbility(it.name, it.isHidden) },
        types = pokemon.typesCsv?.split(',')?.filter { it.isNotBlank() }.orEmpty(),
        height = pokemon.height,
        weight = pokemon.weight,
        category = pokemon.category
    )
}

private fun PokemonSpeciesDto.englishFlavorText(): String? {
    return flavorTextEntries
        .firstOrNull { it.language.name == "en" }
        ?.flavorText
        ?.replace("\n", " ")
        ?.replace("\f", " ")
        ?.trim()
}

private fun PokemonSpeciesDto.englishGenus(): String? {
    return genera.firstOrNull { it.language.name == "en" }?.genus
}

private fun extractIdFromUrl(url: String): Int {
    return url.trimEnd('/').substringAfterLast('/').toIntOrNull() ?: 0
}
