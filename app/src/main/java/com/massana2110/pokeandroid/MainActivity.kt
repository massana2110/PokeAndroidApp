package com.massana2110.pokeandroid

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.google.android.material.snackbar.Snackbar
import com.massana2110.pokeandroid.app.services.PokemonService
import com.massana2110.pokeandroid.databinding.ActivityMainBinding
import com.massana2110.pokeandroid.presentation.adapter.PokemonListAdapter
import com.massana2110.pokeandroid.presentation.viewmodels.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var pokemonAdapter: PokemonListAdapter
    private val mainViewModel by viewModels<MainViewModel>()
    private var intentService: Intent? = null

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (isGranted) {
                // Permiso concedido, puedes mostrar notificaciones
                // Iniciar el servicio
                intentService = Intent(this, PokemonService::class.java)
                startService(intentService)
            } else {
                // Permiso denegado, maneja esto en consecuencia
                Toast.makeText(this, "Se requiere permiso de notificaciones", Toast.LENGTH_SHORT)
                    .show()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        requestNotificationPermission()
        mainViewModel.getPokemonList()

        initViews()
        setupObservers()
    }

    override fun onDestroy() {
        super.onDestroy()
        if (intentService != null)
            stopService(intentService)
    }

    private fun initViews() {
        pokemonAdapter = PokemonListAdapter(emptyList())
        binding.pokemonListRecyclerView.adapter = pokemonAdapter
    }

    private fun setupObservers() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                mainViewModel.uiState.collect { uiState ->
                    binding.loaderPokemon.visibility =
                        if (uiState.isLoading) View.VISIBLE else View.GONE

                    if (uiState.pokemonList.isNotEmpty()) {
                        pokemonAdapter.updateList(uiState.pokemonList)
                    }
                }
            }
        }
    }

    private fun requestNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            when {
                ContextCompat.checkSelfPermission(
                    this,
                    android.Manifest.permission.POST_NOTIFICATIONS
                ) == PackageManager.PERMISSION_GRANTED -> {
                    Toast.makeText(this, "Permiso concedido", Toast.LENGTH_SHORT)
                        .show()

                    intentService = Intent(this, PokemonService::class.java)
                    startService(intentService)
                }

                else -> {
                    // Solicitar el permiso
                    requestPermissionLauncher.launch(android.Manifest.permission.POST_NOTIFICATIONS)
                }
            }
        }
    }
}