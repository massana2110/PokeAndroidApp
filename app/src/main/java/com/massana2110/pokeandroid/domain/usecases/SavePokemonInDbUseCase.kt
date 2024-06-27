package com.massana2110.pokeandroid.domain.usecases

import com.massana2110.pokeandroid.data.datasources.database.entities.PokemonTypeCrossEntity
import com.massana2110.pokeandroid.data.mappers.toPokemonEntity
import com.massana2110.pokeandroid.data.repositories.PokemonRepository
import com.massana2110.pokeandroid.domain.models.PokemonItemModel
import javax.inject.Inject

class SavePokemonInDbUseCase @Inject constructor(
    private val pokemonRepository: PokemonRepository
) {

    suspend operator fun invoke(pokemonList: List<PokemonItemModel>): Result<Long> =
        pokemonRepository.insertAllPokemonWithTypes(
            listPokemon = pokemonList.map { it.toPokemonEntity() },
            listPokemonType = getListPokemonTypeCrossEntity(pokemonList)
        )

    private fun getListPokemonTypeCrossEntity(
        pokemonList: List<PokemonItemModel>
    ): List<PokemonTypeCrossEntity> {
        val pokemonTypeList = mutableListOf<PokemonTypeCrossEntity>()

        pokemonList.forEach { pokemon ->
            pokemon.pokemonTypes.forEach {
                pokemonTypeList.add(
                    PokemonTypeCrossEntity(
                        pokemonId = pokemon.pokemonId,
                        typeId = it.ordinal
                    )
                )
            }
        }

        return pokemonTypeList.toList()
    }
}