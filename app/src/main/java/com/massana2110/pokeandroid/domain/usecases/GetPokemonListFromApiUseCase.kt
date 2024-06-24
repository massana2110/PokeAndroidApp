package com.massana2110.pokeandroid.domain.usecases

import com.massana2110.pokeandroid.data.datasources.network.PokeApiResult
import com.massana2110.pokeandroid.data.datasources.network.models.PokemonDetailResponse
import com.massana2110.pokeandroid.data.datasources.network.models.PokemonListResponse
import com.massana2110.pokeandroid.data.repositories.PokemonRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GetPokemonListFromApiUseCase @Inject constructor(
    private val pokemonRepository: PokemonRepository
) {

    // Create a CoroutineScope with a SupervisorJob
    private val coroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    suspend operator fun invoke(limit: Int, offset: Int): Result<List<PokemonDetailResponse>>? =
        coroutineScope.async {
            when (val result = pokemonRepository.getPokemonList(limit, offset)) {
                is PokeApiResult.Success -> {
                    val pokemonUrls = result.data?.results?.map { it.url }

                    pokemonUrls?.let { urls ->
                        val deferredPokemonDetails = urls.map { url ->
                            val pokemonId = url.split("/").dropLast(1).last().toInt()
                            async { pokemonRepository.fetchPokemonDetailById(pokemonId) }
                        }

                        val pokemonResultList = mutableListOf<PokemonDetailResponse>()
                        val pokemonDetailsList = deferredPokemonDetails.awaitAll()

                        pokemonDetailsList.forEach { apiResult ->
                            if (apiResult is PokeApiResult.Success)
                                pokemonResultList.add(apiResult.data!!)
                        }

                        Result.success(pokemonResultList.toList())
                    }
                }
                is PokeApiResult.Error -> {
                    Result.failure(Throwable(result.message))
                }
                is PokeApiResult.Exception -> {
                    Result.failure(result.exception)
                }
            }
        }.await()

}