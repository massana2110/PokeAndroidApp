package com.massana2110.pokeandroid.data.datasources.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import com.massana2110.pokeandroid.data.datasources.database.entities.PokemonTypeEntity

@Dao
interface PokemonTypeDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTypes(typesList: List<PokemonTypeEntity>): List<Long>
}