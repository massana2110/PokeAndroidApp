package com.massana2110.pokeandroid.presentation.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.massana2110.pokeandroid.domain.mappers.toDomain
import com.massana2110.pokeandroid.domain.models.PokemonItemModel
import com.massana2110.pokeandroid.domain.usecases.GetPokemonListFromApiUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val getPokemonListFromApiUseCase: GetPokemonListFromApiUseCase
): ViewModel() {

    private val _pokemonList = MutableLiveData<List<PokemonItemModel>>()
    val pokemonList: LiveData<List<PokemonItemModel>> = _pokemonList

    fun fetchPokemonList() {
        viewModelScope.launch {
            getPokemonListFromApiUseCase(20, 20)
                ?.onSuccess { _pokemonList.postValue(it.map { pokemon -> pokemon.toDomain() }) }
                ?.onFailure { println(it.message) }
        }
    }

}