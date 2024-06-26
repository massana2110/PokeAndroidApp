package com.massana2110.pokeandroid.data.datasources.network

import com.massana2110.pokeandroid.data.datasources.network.models.PokemonDetailResponse
import com.massana2110.pokeandroid.data.datasources.network.models.PokemonListResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface PokeAndroidClient {

    // Define api request methods here
    @GET("pokemon")
    suspend fun getPokemonList(
        @Query("limit") limit: Int,
        @Query("offset") offset: Int
    ): Response<PokemonListResponse>

    @GET("pokemon/{id}")
    suspend fun getPokemonDetail(
        @Path("id") id: Int
    ): Response<PokemonDetailResponse>
}