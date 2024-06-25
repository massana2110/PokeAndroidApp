package com.massana2110.pokeandroid.domain.usecases

import com.massana2110.pokeandroid.data.repositories.PokemonRepository
import com.massana2110.pokeandroid.domain.mappers.toDomain
import com.massana2110.pokeandroid.domain.models.PokemonItemModel
import com.massana2110.pokeandroid.domain.models.PokemonTypesEnumModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetPokemonListFromDbUseCase @Inject constructor(
    private val pokemonRepository: PokemonRepository
) {

    operator fun invoke(): Flow<List<PokemonItemModel>> = pokemonRepository.getPokemonList()
        .map { pokemonList ->
            pokemonList.map {
                PokemonItemModel(
                    pokemonId = it.pokemon.pokemonId,
                    pokemonName = it.pokemon.name,
                    pokemonTypes = it.types.map { type ->
                        type.toDomain() ?: PokemonTypesEnumModel.NORMAL
                    },
                    pokemonSprite = it.pokemon.pokemonSprite
                )
            }
        }
}