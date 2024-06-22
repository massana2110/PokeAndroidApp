package com.massana2110.pokeandroid.domain.usecases

import com.massana2110.pokeandroid.data.datasources.network.PokeApiResult
import com.massana2110.pokeandroid.data.repositories.PokemonRepository
import javax.inject.Inject

class GetPokemonListFromApiUseCase @Inject constructor(
    private val pokemonRepository: PokemonRepository
) {

    suspend operator fun invoke(limit: Int, offset: Int): Result<List<String>?> {
        return when (val result = pokemonRepository.getPokemonList(limit, offset)) {
            is PokeApiResult.Success -> {
                Result.success(result.data?.results?.map { it.url })
            }
            is PokeApiResult.Error -> {
                Result.failure(Throwable(result.message))
            }
            is PokeApiResult.Exception -> {
                Result.failure(result.exception)
            }
        }
    }
}