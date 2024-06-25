package com.massana2110.pokeandroid.data.datasources.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "pokemon_table")
data class PokemonEntity(
    @PrimaryKey(autoGenerate = false) val pokemonId: Int,
    val name: String,
    val pokemonSprite: String
)
