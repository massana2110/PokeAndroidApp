package com.massana2110.pokeandroid

import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
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

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (isGranted) {
                // Permiso concedido, puedes mostrar notificaciones
            } else {
                // Permiso denegado, maneja esto en consecuencia
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        requestNotificationPermission()

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

    private fun requestNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            when {
                ContextCompat.checkSelfPermission(
                    this,
                    android.Manifest.permission.POST_NOTIFICATIONS
                ) == PackageManager.PERMISSION_GRANTED -> {
                    Toast.makeText(this, "Permiso concedido", Toast.LENGTH_SHORT)
                        .show()
                }
                else -> {
                    // Solicitar el permiso
                    requestPermissionLauncher.launch(android.Manifest.permission.POST_NOTIFICATIONS)
                }
            }
        }
    }
}