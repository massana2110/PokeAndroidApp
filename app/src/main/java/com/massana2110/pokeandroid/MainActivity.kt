package com.massana2110.pokeandroid

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.massana2110.pokeandroid.databinding.ActivityMainBinding
import com.massana2110.pokeandroid.presentation.adapter.PokemonListAdapter
import com.massana2110.pokeandroid.presentation.viewmodels.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var pokemonAdapter: PokemonListAdapter
    private val mainViewModel by viewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mainViewModel.fetchPokemonList()

        initViews()
        setupObservers()
    }

    private fun initViews() {
        pokemonAdapter = PokemonListAdapter(emptyList())
        binding.pokemonListRecyclerView.adapter = pokemonAdapter
    }

    private fun setupObservers() {
        mainViewModel.pokemonList.observe(this) {
            if (!it.isNullOrEmpty()) {
                pokemonAdapter.updateList(it)
            }
        }
    }
}