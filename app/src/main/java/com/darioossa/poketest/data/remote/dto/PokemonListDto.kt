package com.darioossa.poketest.data.remote.dto

import com.squareup.moshi.Json


data class PokemonListResponseDto(
    @Json(name = "count") val count: Int,
    @Json(name = "next") val next: String?,
    @Json(name = "previous") val previous: String?,
    @Json(name = "results") val results: List<NamedApiResourceDto>
)

data class NamedApiResourceDto(
    @Json(name = "name") val name: String,
    @Json(name = "url") val url: String
)
