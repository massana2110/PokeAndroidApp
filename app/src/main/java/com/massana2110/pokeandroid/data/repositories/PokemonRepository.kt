package com.massana2110.pokeandroid.data.repositories

import com.massana2110.pokeandroid.data.datasources.database.dao.PokemonDao
import com.massana2110.pokeandroid.data.datasources.database.dao.PokemonTypeDao
import com.massana2110.pokeandroid.data.datasources.database.entities.PokemonEntity
import com.massana2110.pokeandroid.data.datasources.database.entities.PokemonTypeCrossEntity
import com.massana2110.pokeandroid.data.datasources.database.entities.PokemonTypeEntity
import com.massana2110.pokeandroid.data.datasources.network.PokeAndroidClient
import com.massana2110.pokeandroid.data.datasources.network.apiRequest
import javax.inject.Inject

class PokemonRepository @Inject constructor(
    private val pokeAndroidClient: PokeAndroidClient,
    private val pokemonDao: PokemonDao,
    private val pokemonTypeDao: PokemonTypeDao
) {

    // Network calls
    suspend fun getPokemonList(limit: Int, offset: Int) = apiRequest {
        pokeAndroidClient.getPokemonList(limit, offset)
    }

    suspend fun fetchPokemonDetailById(id: Int) = apiRequest {
        pokeAndroidClient.getPokemonDetail(id)
    }

    // Database operations
    suspend fun insertAllTypes(
        listPokemonType: List<PokemonTypeEntity>
    ) {
        pokemonTypeDao.insertTypes(listPokemonType)
    }

    suspend fun insertAllPokemonWithTypes(
        listPokemon: List<PokemonEntity>,
        listPokemonType: List<PokemonTypeCrossEntity>
    ): Result<Unit> {
        return try {
            pokemonDao.insertAllPokemon(listPokemon)
            pokemonDao.insertAllPokemonTypesCross(listPokemonType)
            Result.success(Unit)
        } catch (e: Exception) {
            println(e)
            Result.failure(e)
        }
    }
}