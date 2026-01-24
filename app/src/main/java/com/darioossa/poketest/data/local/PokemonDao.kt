package com.darioossa.poketest.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction

@Dao
interface PokemonDao {
    @Query("SELECT * FROM pokemon ORDER BY id LIMIT :limit OFFSET :offset")
    suspend fun getPokemonList(limit: Int, offset: Int): List<PokemonEntity>

    @Transaction
    @Query("SELECT * FROM pokemon WHERE id = :id")
    suspend fun getPokemonWithDetails(id: Int): PokemonWithDetails?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPokemon(entities: List<PokemonEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStats(stats: List<PokemonStatEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAbilities(abilities: List<PokemonAbilityEntity>)

    @Query("DELETE FROM pokemon_stat WHERE pokemonId = :pokemonId")
    suspend fun clearStatsForPokemon(pokemonId: Int)

    @Query("DELETE FROM pokemon_ability WHERE pokemonId = :pokemonId")
    suspend fun clearAbilitiesForPokemon(pokemonId: Int)

    @Query("DELETE FROM pokemon")
    suspend fun clearAllPokemon()
}
