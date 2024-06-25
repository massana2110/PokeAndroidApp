package com.massana2110.pokeandroid.presentation.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.massana2110.pokeandroid.domain.mappers.toDomain
import com.massana2110.pokeandroid.domain.models.PokemonItemModel
import com.massana2110.pokeandroid.domain.models.PokemonTypesEnumModel
import com.massana2110.pokeandroid.domain.usecases.GetPokemonListFromApiUseCase
import com.massana2110.pokeandroid.domain.usecases.GetPokemonListFromDbUseCase
import com.massana2110.pokeandroid.domain.usecases.SavePokemonTypeInDbUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
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

data class MainUiState(
    val pokemonList: List<PokemonItemModel> = emptyList(),
    val isLoading: Boolean = true
)

@HiltViewModel
class MainViewModel @Inject constructor(
    private val getPokemonListFromDbUseCase: GetPokemonListFromDbUseCase,
    private val savePokemonTypeInDbUseCase: SavePokemonTypeInDbUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(MainUiState())
    val uiState: StateFlow<MainUiState> = _uiState

    init {
        viewModelScope.launch(Dispatchers.IO) {
            savePokemonTypeInDbUseCase(listTypesToInsert)
        }
    }

    fun getPokemonList() {
        viewModelScope.launch {
            getPokemonListFromDbUseCase()
                .catch { println("Error") }
                .collect { pokemonList ->
                _uiState.update { it.copy(pokemonList = pokemonList, isLoading = false) }
            }
        }
    }

}