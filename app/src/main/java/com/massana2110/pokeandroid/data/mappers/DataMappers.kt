package com.massana2110.pokeandroid.data.mappers

import com.massana2110.pokeandroid.data.datasources.database.entities.PokemonEntity
import com.massana2110.pokeandroid.data.datasources.database.entities.PokemonTypeEntity
import com.massana2110.pokeandroid.domain.models.PokemonItemModel
import com.massana2110.pokeandroid.domain.models.PokemonTypesEnumModel

fun PokemonItemModel.toPokemonEntity() = PokemonEntity(
    pokemonId = pokemonId,
    name = pokemonName,
    pokemonSprite = pokemonSprite
)

fun PokemonTypesEnumModel.toPokemonTypeEntity() = PokemonTypeEntity(
    typeId = this.ordinal,
    typeName = this.displayName,
    typeColor = this.colorHex,
    iconResId = this.iconDrawableResId
)