package com.massana2110.pokeandroid

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
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
    private var typesAreSaved: Boolean = false
    private var notificationsAreAllowed = false

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (isGranted) {
                // Permission is granted, nothing to do here
                Toast.makeText(this, "Permiso concedido", Toast.LENGTH_SHORT).show()
                notificationsAreAllowed = true
                checkConditionsToInitiateService()
            }
            else
            // permission is denied, request again
                Toast.makeText(this, "Permiso denegado", Toast.LENGTH_SHORT).show()
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        requestNotificationPermission()

        mainViewModel.setupSearchWithPokemonUpdates()
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

        // Add text watcher to search text field
        binding.searchPokemonTextField.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                mainViewModel.setSearchQuery(s.toString())
            }

            override fun afterTextChanged(s: Editable?) {}

        })
    }

    /**
     * Observer for ui state flow, this handle all the updates in the screen safely
     * in the lifecycle.
     */
    private fun setupObservers() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                mainViewModel.uiState.collect { uiState ->
                    binding.loaderPokemon.visibility =
                        if (uiState.isLoading) View.VISIBLE else View.GONE

                    if (uiState.pokemonList.isNotEmpty()) {
                        pokemonAdapter.updateList(uiState.pokemonList)
                    }

                    if (uiState.error.isNotEmpty()) {
                        showErrorsDialog(uiState.error)
                    }
                }
            }
        }

        mainViewModel.typesAreSaved.observe(this) {
            if (it) {
                typesAreSaved = true
                checkConditionsToInitiateService()
            }
        }
    }

    private fun checkConditionsToInitiateService() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (notificationsAreAllowed && typesAreSaved) initService()
        } else {
            if (typesAreSaved) initService()
        }
    }

    private fun initService() {
        intentService = Intent(this, PokemonService::class.java)
        startService(intentService)
    }

    /**
     * Check and request notification permission on Android 13+ Devices
     * otherwise start service
     */
    private fun requestNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            when {
                ContextCompat.checkSelfPermission(
                    this,
                    android.Manifest.permission.POST_NOTIFICATIONS
                ) == PackageManager.PERMISSION_GRANTED -> {
                    intentService = Intent(this, PokemonService::class.java)
                    startService(intentService)
                }

                shouldShowRequestPermissionRationale(android.Manifest.permission.POST_NOTIFICATIONS) -> {
                    // Explain to the user why the permission is needed
                    showPermissionRationaleDialog()
                }

                else -> {
                    // Request notifications permission
                    requestPermissionLauncher.launch(android.Manifest.permission.POST_NOTIFICATIONS)
                }
            }
        } else {
            checkConditionsToInitiateService()
        }
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun showPermissionRationaleDialog() {
        AlertDialog.Builder(this)
            .setTitle("Permiso de notificaciones necesario")
            .setMessage("Nuestra pokedex necesita de este permiso para obtener los pokemon")
            .setPositiveButton("OK") { _, _ ->
                requestPermissionLauncher.launch(android.Manifest.permission.POST_NOTIFICATIONS)
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
                Toast.makeText(this, "Permiso denegado", Toast.LENGTH_SHORT).show()
            }
            .show()
    }

    private fun showErrorsDialog(message: String) {
        AlertDialog.Builder(this)
            .setTitle("Error")
            .setMessage(message)
            .setPositiveButton("OK") { dialog, _ -> dialog.dismiss() }
            .show()
    }
}