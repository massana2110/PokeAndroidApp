package com.massana2110.pokeandroid.data.datasources.database.entities

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Junction
import androidx.room.PrimaryKey
import androidx.room.Relation

@Entity(
    tableName = "pokemon_type_cross_entity",
    primaryKeys = ["pokemonId", "typeId"]
)
data class PokemonTypeCrossEntity(
    val pokemonId: Int,
    val typeId: Int
)

// Relation
data class PokemonWithTypes(
    @Embedded val pokemon: PokemonEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "typeId",
        associateBy = Junction(PokemonTypeCrossEntity::class)
    )
    val types: List<PokemonTypeEntity>
)