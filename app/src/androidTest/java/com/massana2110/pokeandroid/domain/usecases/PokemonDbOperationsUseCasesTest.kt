package com.massana2110.pokeandroid.domain.usecases

import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.massana2110.pokeandroid.data.datasources.database.PokeAndroidDatabase
import com.massana2110.pokeandroid.data.datasources.network.PokeAndroidClient
import com.massana2110.pokeandroid.data.repositories.PokemonRepository
import com.massana2110.pokeandroid.domain.models.PokemonItemModel
import com.massana2110.pokeandroid.domain.models.PokemonTypesEnumModel
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.RelaxedMockK
import junit.framework.TestCase
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Test class that handle the pokemon operations in db such as
 * insertion and read information pokemon list.
 */
@RunWith(AndroidJUnit4::class)
class PokemonDbOperationsUseCasesTest {

    @RelaxedMockK private lateinit var pokemonApiClient: PokeAndroidClient
    private lateinit var pokemonDatabase: PokeAndroidDatabase
    private lateinit var pokemonRepository: PokemonRepository
    private lateinit var savePokemonInDbUseCase: SavePokemonInDbUseCase
    private lateinit var getPokemonListFromDbUseCase: GetPokemonListFromDbUseCase

    @Before
    fun onBefore() {
        MockKAnnotations.init(this)
        pokemonDatabase = Room.inMemoryDatabaseBuilder(
            InstrumentationRegistry.getInstrumentation().targetContext,
            PokeAndroidDatabase::class.java
        ).build()
        pokemonRepository = PokemonRepository(
            pokemonApiClient,
            pokemonDatabase.getPokemonDao(),
            pokemonDatabase.getPokemonTypeDao()
        )
        savePokemonInDbUseCase = SavePokemonInDbUseCase(pokemonRepository)
        getPokemonListFromDbUseCase = GetPokemonListFromDbUseCase(pokemonRepository)
    }

    @Test
    fun when_insertion_list_in_db_is_successful_then_return_the_saved_list_size() = runBlocking {
        // Given
        val pokemonList = listOf(
            PokemonItemModel(
                pokemonId = 1,
                pokemonName = "Bulbasaur",
                pokemonSprite = "some_url",
                pokemonTypes = listOf(PokemonTypesEnumModel.GRASS, PokemonTypesEnumModel.POISON)
            ),
            PokemonItemModel(
                pokemonId = 2,
                pokemonName = "Ivysaur",
                pokemonSprite = "other_url",
                pokemonTypes = listOf(PokemonTypesEnumModel.GRASS, PokemonTypesEnumModel.POISON)
            )
        )

        // When
        val result = savePokemonInDbUseCase(pokemonList)
        val resultListDb = getPokemonListFromDbUseCase()

        // Then
        TestCase.assertTrue(result.isSuccess) // Insertion process was successful
        assertEquals(2L, result.getOrNull()) // expected rows affected in pokemon table insertion
        assertEquals(pokemonList.size, resultListDb.first().size) // assertion pokemon list size is equals
    }

    @After
    fun teardown() {
        pokemonDatabase.close()
    }

}