package com.darioossa.poketest.testdata

import com.darioossa.poketest.domain.model.PokemonAbility
import com.darioossa.poketest.domain.model.PokemonDetail
import com.darioossa.poketest.domain.model.PokemonStat
import com.darioossa.poketest.domain.model.PokemonSummary

object PokedexTestData {
    val pikachuSummary = PokemonSummary(
        id = 25,
        name = "pikachu",
        imageUrl = "https://example.com/pikachu.png",
        types = listOf("electric"),
        isFavorite = false
    )

    val bulbasaurSummary = PokemonSummary(
        id = 1,
        name = "bulbasaur",
        imageUrl = "https://example.com/bulbasaur.png",
        types = listOf("grass", "poison"),
        isFavorite = true
    )

    val pikachuDetail = PokemonDetail(
        id = 25,
        name = "pikachu",
        imageUrl = "https://example.com/pikachu.png",
        description = "A yellow electric mouse.",
        stats = listOf(
            PokemonStat("hp", 35),
            PokemonStat("attack", 55)
        ),
        abilities = listOf(
            PokemonAbility("static", false)
        ),
        types = listOf("electric"),
        height = 4,
        weight = 60,
        category = "Mouse Pokemon"
    )
}
