package com.darioossa.poketest.data.local

class PokeLocalDataSource(
    private val dao: PokemonDao
) {
    suspend fun getPokemonList(limit: Int, offset: Int): List<PokemonEntity> {
        return dao.getPokemonList(limit, offset)
    }

    suspend fun getPokemonWithDetails(id: Int): PokemonWithDetails? {
        return dao.getPokemonWithDetails(id)
    }

    suspend fun savePokemonList(entities: List<PokemonEntity>) {
        dao.insertPokemon(entities)
    }

    suspend fun savePokemonDetails(
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
