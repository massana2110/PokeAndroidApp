package com.massana2110.pokeandroid.data.datasources.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.massana2110.pokeandroid.data.datasources.database.entities.PokemonEntity
import com.massana2110.pokeandroid.data.datasources.database.entities.PokemonTypeCrossEntity
import com.massana2110.pokeandroid.data.datasources.database.entities.PokemonWithTypes
import kotlinx.coroutines.flow.Flow

@Dao
interface PokemonDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllPokemon(pokemonList: List<PokemonEntity>)

    @Insert
    suspend fun insertAllPokemonTypesCross(
        pokemonWithTypesList: List<PokemonTypeCrossEntity>
    )

    @Query("SELECT * FROM pokemon_table")
    fun getAllPokemonWithTypes(): Flow<List<PokemonWithTypes>>
}