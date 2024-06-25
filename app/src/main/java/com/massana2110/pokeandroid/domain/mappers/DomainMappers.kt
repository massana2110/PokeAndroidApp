package com.massana2110.pokeandroid.domain.mappers

import com.massana2110.pokeandroid.data.datasources.database.entities.PokemonTypeEntity
import com.massana2110.pokeandroid.data.datasources.network.models.PokemonDetailResponse
import com.massana2110.pokeandroid.domain.models.PokemonItemModel
import com.massana2110.pokeandroid.domain.models.PokemonTypesEnumModel

fun PokemonDetailResponse.toDomain() = PokemonItemModel(
    pokemonId = id ?: 0,
    pokemonName = name.toString(),
    pokemonTypes = types.map { type ->
        when(type.type?.name) {
            "normal" -> PokemonTypesEnumModel.NORMAL
            "fighting" -> PokemonTypesEnumModel.FIGHTING
            "flying" -> PokemonTypesEnumModel.FLYING
            "poison" -> PokemonTypesEnumModel.POISON
            "ground" -> PokemonTypesEnumModel.GROUND
            "rock" -> PokemonTypesEnumModel.ROCK
            "bug" -> PokemonTypesEnumModel.BUG
            "ghost" -> PokemonTypesEnumModel.GHOST
            "steel" -> PokemonTypesEnumModel.STEEL
            "fire" -> PokemonTypesEnumModel.FIRE
            "water" -> PokemonTypesEnumModel.WATER
            "grass" -> PokemonTypesEnumModel.GRASS
            "electric" -> PokemonTypesEnumModel.ELECTRIC
            "psychic" -> PokemonTypesEnumModel.PSYCHIC
            "ice" -> PokemonTypesEnumModel.ICE
            "dragon" -> PokemonTypesEnumModel.DRAGON
            "dark" -> PokemonTypesEnumModel.DARK
            "fairy" -> PokemonTypesEnumModel.FAIRY
            else -> PokemonTypesEnumModel.NORMAL
        }
    },
    pokemonSprite = sprites?.other?.home?.frontDefault ?: ""
)

fun PokemonTypeEntity.toDomain() = PokemonTypesEnumModel.fromOrdinal(this.typeId)