package com.massana2110.pokeandroid.domain.usecases

import com.massana2110.pokeandroid.data.repositories.PokemonRepository
import javax.inject.Inject

class GetPokemonCountDbUseCase @Inject constructor(
    private val pokemonRepository: PokemonRepository
) {

    suspend operator fun invoke(): Result<Int> = pokemonRepository.getPokemonCount()
}