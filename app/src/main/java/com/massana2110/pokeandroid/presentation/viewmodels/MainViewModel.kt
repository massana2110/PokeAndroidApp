package com.massana2110.pokeandroid.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.massana2110.pokeandroid.domain.usecases.GetPokemonListFromApiUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val getPokemonListFromApiUseCase: GetPokemonListFromApiUseCase
): ViewModel() {

    fun fetchPokemonList() {
        viewModelScope.launch {
            getPokemonListFromApiUseCase(20, 20)
                .onSuccess { println(it) }
                .onFailure { println(it.message) }
        }
    }

}