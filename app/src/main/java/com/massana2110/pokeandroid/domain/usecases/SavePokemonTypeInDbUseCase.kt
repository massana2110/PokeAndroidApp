package com.massana2110.pokeandroid.domain.usecases

import com.massana2110.pokeandroid.data.mappers.toPokemonTypeEntity
import com.massana2110.pokeandroid.data.repositories.PokemonRepository
import com.massana2110.pokeandroid.domain.models.PokemonTypesEnumModel
import javax.inject.Inject

class SavePokemonTypeInDbUseCase @Inject constructor(
    private val pokemonRepository: PokemonRepository
) {

    suspend operator fun invoke(typesList: List<PokemonTypesEnumModel>) {
        pokemonRepository.insertAllTypes(typesList.map { it.toPokemonTypeEntity() })
    }

}