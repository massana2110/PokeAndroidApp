package com.massana2110.pokeandroid.data.datasources.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "pokemon_type_table")
data class PokemonTypeEntity(
    @PrimaryKey val typeId: Int,
    val typeName: String,
    val typeColor: String,
    val iconResId: Int
)
