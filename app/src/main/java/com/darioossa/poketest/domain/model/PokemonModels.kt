package com.darioossa.poketest.domain.model

data class PokemonSummary(
    val id: Int,
    val name: String,
    val imageUrl: String,
    val types: List<String> = emptyList(),
    val isFavorite: Boolean
)

data class PokemonDetail(
    val id: Int,
    val name: String,
    val imageUrl: String,
    val description: String?,
    val stats: List<PokemonStat>,
    val abilities: List<PokemonAbility>,
    val types: List<String>,
    val height: Int?,
    val weight: Int?,
    val category: String?
)

data class PokemonStat(
    val name: String,
    val value: Int
)

data class PokemonAbility(
    val name: String,
    val isHidden: Boolean
)
