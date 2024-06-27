package com.massana2110.pokeandroid.presentation.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.massana2110.pokeandroid.domain.models.PokemonItemModel
import com.massana2110.pokeandroid.domain.models.PokemonTypesEnumModel
import com.massana2110.pokeandroid.domain.usecases.GetPokemonListFromDbUseCase
import com.massana2110.pokeandroid.domain.usecases.SavePokemonTypeInDbUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
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

/**
 * Main Activity UI State managed by viewmodel
 */
data class MainUiState(
    val pokemonList: List<PokemonItemModel> = emptyList(),
    val isLoading: Boolean = true,
    val error: String = ""
)

@HiltViewModel
class MainViewModel @Inject constructor(
    private val getPokemonListFromDbUseCase: GetPokemonListFromDbUseCase,
    private val savePokemonTypeInDbUseCase: SavePokemonTypeInDbUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(MainUiState())
    val uiState: StateFlow<MainUiState> = _uiState

    private val _typesAreSaved = MutableLiveData(false)
    val typesAreSaved: LiveData<Boolean> = _typesAreSaved

    // mutable variables only accessible from this viewmodel
    private val _pokemonList = MutableStateFlow<List<PokemonItemModel>>(emptyList())
    private val _searchQuery = MutableStateFlow("")

    init {
        viewModelScope.launch(Dispatchers.IO) {
            savePokemonTypeInDbUseCase(listTypesToInsert)
                .onSuccess {
                    println("TYPES OF POKEMON SAVED")
                    _typesAreSaved.postValue(true)
                }
        }
    }

    /**
     * Combine the pokemon updates from service with the current list and
     * search query and expose this result to the main ui state flow that
     * activity consumes
     */
    fun setupSearchWithPokemonUpdates() {
        viewModelScope.launch {
            combine(_searchQuery, _pokemonList) { query, pokemonList ->
                if (query.isBlank()) {
                    pokemonList
                } else {
                    pokemonList.filter { pokemon ->
                        pokemon.pokemonName.contains(query, ignoreCase = true) ||
                                pokemon.pokemonTypes.any { it.displayName.contains(query, ignoreCase = true) }
                    }
                }
            }.catch { e ->
                // Catching error on pokemon get failed
                _uiState.update { it.copy(
                    error = "Ocurrió un error obteniendo la lista de Pokémon: ${e.message}")
                }
            }.collect { filteredList ->
                _uiState.update {
                    it.copy(
                        pokemonList = filteredList,
                        isLoading = false,
                        error = ""
                    )
                }
            }
        }
    }

    /**
     * Init the app with the pokemon list saved in DB
     */
    fun getPokemonList() {
        viewModelScope.launch {
            getPokemonListFromDbUseCase()
                .catch {
                    _uiState.update { it.copy(error = "Ocurrio un error obteniendo la lista de pokemon") }
                }
                .collect { pokemonList ->
                    _pokemonList.value = pokemonList
                }
        }
    }

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }
}