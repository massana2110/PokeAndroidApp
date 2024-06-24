package com.massana2110.pokeandroid.data.datasources.network.models

import com.squareup.moshi.Json

data class PokemonDetailResponse(
    @Json(name = "id") val id: Int?,
    @Json(name = "name") val name: String?,
    @Json(name = "sprites") val sprites: SpritesResponse?,
    @Json(name = "types") val types: List<PokemonTypesResponse>
)

data class SpritesResponse(
    @Json(name = "other") val other: OtherSpriteResponse?
)

data class OtherSpriteResponse(
    @Json(name = "home") val home: HomeSpriteResponse
)

data class HomeSpriteResponse(
    @Json(name = "front_default") val frontDefault: String
)

data class PokemonTypesResponse(
    @Json(name = "type") val type: PokemonTypeResponse?
)

data class PokemonTypeResponse(
    @Json(name = "name") val name: String?
)