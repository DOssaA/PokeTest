package com.darioossa.poketest.data.local

interface PokeLocalDataSource {
    suspend fun getPokemonList(limit: Int, offset: Int): List<PokemonEntity>

    suspend fun getPokemonWithDetails(id: Int): PokemonWithDetails?

    suspend fun savePokemonList(entities: List<PokemonEntity>)

    suspend fun savePokemonDetails(
        pokemon: PokemonEntity,
        stats: List<PokemonStatEntity>,
        abilities: List<PokemonAbilityEntity>
    )
}

class PokeLocalDataSourceImpl(
    private val dao: PokemonDao
) : PokeLocalDataSource {
    override suspend fun getPokemonList(limit: Int, offset: Int): List<PokemonEntity> {
        return dao.getPokemonList(limit, offset)
    }

    override suspend fun getPokemonWithDetails(id: Int): PokemonWithDetails? {
        return dao.getPokemonWithDetails(id)
    }

    override suspend fun savePokemonList(entities: List<PokemonEntity>) {
        dao.insertPokemon(entities)
    }

    override suspend fun savePokemonDetails(
        pokemon: PokemonEntity,
        stats: List<PokemonStatEntity>,
        abilities: List<PokemonAbilityEntity>
    ) {
        dao.insertPokemon(listOf(pokemon))
        dao.clearStatsForPokemon(pokemon.id)
        dao.clearAbilitiesForPokemon(pokemon.id)
        if (stats.isNotEmpty()) {
            dao.insertStats(stats)
        }
        if (abilities.isNotEmpty()) {
            dao.insertAbilities(abilities)
        }
    }
}
