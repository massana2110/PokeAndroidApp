package com.massana2110.pokeandroid.data.datasources.network.models

import com.squareup.moshi.Json

data class PokemonListResponse(
    @Json(name = "results") val results: List<PokemonBasicInfo>
)

data class PokemonBasicInfo(
    @Json(name = "name") val name: String,
    @Json(name = "url") val url: String
)