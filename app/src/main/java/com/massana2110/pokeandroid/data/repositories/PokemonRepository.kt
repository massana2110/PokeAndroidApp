package com.massana2110.pokeandroid.data.repositories

import com.massana2110.pokeandroid.data.datasources.network.PokeAndroidClient
import com.massana2110.pokeandroid.data.datasources.network.apiRequest
import javax.inject.Inject

class PokemonRepository @Inject constructor(
    private val pokeAndroidClient: PokeAndroidClient
) {

    // Network calls
    suspend fun getPokemonList(limit: Int, offset: Int) = apiRequest {
        pokeAndroidClient.getPokemonList(limit, offset)
    }

    suspend fun fetchPokemonDetailById(id: Int) = apiRequest {
        pokeAndroidClient.getPokemonDetail(id)
    }

}