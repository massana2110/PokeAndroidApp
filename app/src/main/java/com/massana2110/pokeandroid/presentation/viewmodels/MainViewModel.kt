package com.massana2110.pokeandroid.presentation.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.massana2110.pokeandroid.domain.mappers.toDomain
import com.massana2110.pokeandroid.domain.models.PokemonItemModel
import com.massana2110.pokeandroid.domain.models.PokemonTypesEnumModel
import com.massana2110.pokeandroid.domain.usecases.GetPokemonListFromApiUseCase
import com.massana2110.pokeandroid.domain.usecases.SavePokemonTypeInDbUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

private val listTypesToInsert = listOf(
    PokemonTypesEnumModel.BUG,
    PokemonTypesEnumModel.DARK,
    PokemonTypesEnumModel.DRAGON,
    PokemonTypesEnumModel.ELECTRIC,
    PokemonTypesEnumModel.FAIRY,
    PokemonTypesEnumModel.FIGHTING,
    PokemonTypesEnumModel.FIRE,
    PokemonTypesEnumModel.FLYING,
    PokemonTypesEnumModel.GHOST,
    PokemonTypesEnumModel.GRASS,
    PokemonTypesEnumModel.GROUND,
    PokemonTypesEnumModel.ICE,
    PokemonTypesEnumModel.NORMAL,
    PokemonTypesEnumModel.POISON,
    PokemonTypesEnumModel.PSYCHIC,
    PokemonTypesEnumModel.ROCK,
    PokemonTypesEnumModel.STEEL,
    PokemonTypesEnumModel.WATER
)

@HiltViewModel
class MainViewModel @Inject constructor(
    private val getPokemonListFromApiUseCase: GetPokemonListFromApiUseCase,
    private val savePokemonTypeInDbUseCase: SavePokemonTypeInDbUseCase
): ViewModel() {

    private val _pokemonList = MutableLiveData<List<PokemonItemModel>>()
    val pokemonList: LiveData<List<PokemonItemModel>> = _pokemonList

    init {
        viewModelScope.launch {
            savePokemonTypeInDbUseCase(listTypesToInsert)
        }
    }

    fun fetchPokemonList() {
        viewModelScope.launch {
            getPokemonListFromApiUseCase(20, 20)
                ?.onSuccess { _pokemonList.postValue(it.map { pokemon -> pokemon.toDomain() }) }
                ?.onFailure { println(it.message) }
        }
    }

    private fun saveAllPokemonInLocalDb() {

    }

}