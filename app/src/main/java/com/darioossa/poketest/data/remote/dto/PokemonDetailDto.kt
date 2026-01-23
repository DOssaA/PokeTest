package com.darioossa.poketest.data.remote.dto

import com.squareup.moshi.Json


data class PokemonDetailDto(
    @Json(name = "id") val id: Int,
    @Json(name = "name") val name: String,
    @Json(name = "height") val height: Int,
    @Json(name = "weight") val weight: Int,
    @Json(name = "types") val types: List<PokemonTypeSlotDto>,
    @Json(name = "stats") val stats: List<PokemonStatSlotDto>,
    @Json(name = "abilities") val abilities: List<PokemonAbilitySlotDto>,
    @Json(name = "sprites") val sprites: PokemonSpritesDto
)

data class PokemonTypeSlotDto(
    @Json(name = "type") val type: NamedApiResourceDto
)

data class PokemonStatSlotDto(
    @Json(name = "base_stat") val baseStat: Int,
    @Json(name = "stat") val stat: NamedApiResourceDto
)

data class PokemonAbilitySlotDto(
    @Json(name = "is_hidden") val isHidden: Boolean,
    @Json(name = "ability") val ability: NamedApiResourceDto
)

data class PokemonSpritesDto(
    @Json(name = "front_default") val frontDefault: String?,
    @Json(name = "other") val other: PokemonOtherSpritesDto?
)

data class PokemonOtherSpritesDto(
    @Json(name = "official-artwork") val officialArtwork: PokemonOfficialArtworkDto?
)

data class PokemonOfficialArtworkDto(
    @Json(name = "front_default") val frontDefault: String?
)

data class PokemonSpeciesDto(
    @Json(name = "flavor_text_entries") val flavorTextEntries: List<PokemonFlavorTextDto>,
    @Json(name = "genera") val genera: List<PokemonGenusDto>
)

data class PokemonFlavorTextDto(
    @Json(name = "flavor_text") val flavorText: String,
    @Json(name = "language") val language: NamedApiResourceDto
)

data class PokemonGenusDto(
    @Json(name = "genus") val genus: String,
    @Json(name = "language") val language: NamedApiResourceDto
)
