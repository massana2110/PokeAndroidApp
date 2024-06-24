package com.massana2110.pokeandroid.domain.models

import androidx.annotation.DrawableRes
import com.massana2110.pokeandroid.R

data class PokemonItemModel(
    val pokemonId: Int,
    val pokemonName: String,
    val pokemonTypes: List<PokemonTypesEnumModel>,
    val pokemonSprite: String
)

enum class PokemonTypesEnumModel(
    val displayName: String,
    val colorHex: String,
    @DrawableRes val iconDrawableResId: Int
) {
    BUG("Bicho", "#83C300", R.drawable.ic_type_bug),
    DARK("Siniestro", "#5B5466", R.drawable.ic_type_dark),
    DRAGON("Dragón", "#006FC9", R.drawable.ic_type_dragon),
    ELECTRIC("Eléctrico", "#FBD100", R.drawable.ic_type_electric),
    FAIRY("Hada", "#FB89EB", R.drawable.ic_type_fairy),
    FIGHTING("Lucha", "#E0306A", R.drawable.ic_type_fighting),
    FIRE("Fuego", "#FF9741", R.drawable.ic_type_fire),
    FLYING("Volador", "#89AAE3", R.drawable.ic_type_flying),
    GHOST("Fantasma", "#4C6AB2", R.drawable.ic_type_ghost),
    GRASS("Hierba", "#38BF4B", R.drawable.ic_type_grass),
    GROUND("Tierra", "#E87236", R.drawable.ic_type_ground),
    ICE("Hielo", "#4CD1C0", R.drawable.ic_type_ice),
    NORMAL("Normal", "#919AA2", R.drawable.ic_type_normal),
    POISON("Veneno", "#B567CE", R.drawable.ic_type_poison),
    PSYCHIC("Psiquico", "#FF6675", R.drawable.ic_type_psychic),
    ROCK("Roca", "#C8B686", R.drawable.ic_type_rock),
    STEEL("Acero", "#5A8EA2", R.drawable.ic_type_steel),
    WATER("Agua", "#3692DC", R.drawable.ic_type_water)
}