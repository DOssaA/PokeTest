package com.darioossa.poketest.data.local

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation

@Entity(tableName = "pokemon")
data class PokemonEntity(
    @PrimaryKey val id: Int,
    val name: String,
    val imageUrl: String,
    val typesCsv: String?,
    val height: Int?,
    val weight: Int?,
    val description: String?,
    val category: String?,
    val lastUpdated: Long
)

@Entity(tableName = "pokemon_stat", primaryKeys = ["pokemonId", "name"])
data class PokemonStatEntity(
    val pokemonId: Int,
    val name: String,
    val value: Int
)

@Entity(tableName = "pokemon_ability", primaryKeys = ["pokemonId", "name"])
data class PokemonAbilityEntity(
    val pokemonId: Int,
    val name: String,
    val isHidden: Boolean
)

data class PokemonWithDetails(
    @Embedded val pokemon: PokemonEntity,
    @Relation(parentColumn = "id", entityColumn = "pokemonId")
    val stats: List<PokemonStatEntity>,
    @Relation(parentColumn = "id", entityColumn = "pokemonId")
    val abilities: List<PokemonAbilityEntity>
)
