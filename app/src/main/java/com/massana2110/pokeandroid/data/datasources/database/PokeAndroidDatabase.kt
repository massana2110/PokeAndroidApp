package com.massana2110.pokeandroid.data.datasources.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.massana2110.pokeandroid.data.datasources.database.dao.PokemonDao
import com.massana2110.pokeandroid.data.datasources.database.dao.PokemonTypeDao
import com.massana2110.pokeandroid.data.datasources.database.entities.PokemonEntity
import com.massana2110.pokeandroid.data.datasources.database.entities.PokemonTypeCrossEntity
import com.massana2110.pokeandroid.data.datasources.database.entities.PokemonTypeEntity

@Database(
    entities = [
        PokemonEntity::class,
        PokemonTypeEntity::class,
        PokemonTypeCrossEntity::class],
    version = 1,
    exportSchema = true,
    autoMigrations = []
)
abstract class PokeAndroidDatabase : RoomDatabase() {

    abstract fun getPokemonDao(): PokemonDao
    abstract fun getPokemonTypeDao(): PokemonTypeDao
}