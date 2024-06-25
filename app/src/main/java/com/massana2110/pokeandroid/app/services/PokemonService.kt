package com.massana2110.pokeandroid.app.services

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.massana2110.pokeandroid.R
import com.massana2110.pokeandroid.app.utils.flatMap
import com.massana2110.pokeandroid.domain.mappers.toDomain
import com.massana2110.pokeandroid.domain.usecases.GetPokemonCountDbUseCase
import com.massana2110.pokeandroid.domain.usecases.GetPokemonListFromApiUseCase
import com.massana2110.pokeandroid.domain.usecases.SavePokemonInDbUseCase
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@AndroidEntryPoint
class PokemonService : Service() {

    @Inject
    lateinit var getPokemonCountDbUseCase: GetPokemonCountDbUseCase

    @Inject
    lateinit var getPokemonListFromApiUseCase: GetPokemonListFromApiUseCase

    @Inject
    lateinit var savePokemonInDbUseCase: SavePokemonInDbUseCase

    private lateinit var scheduler: ScheduledExecutorService
    private val serviceScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private var limit = 15
    private var offset = 0

    override fun onCreate() {
        super.onCreate()
        println("INIT POKEMON SERVICE")
        createNotificationChannel()
        startForeground(1, createInitialNotification())

        scheduler = Executors.newSingleThreadScheduledExecutor()
        scheduler.scheduleWithFixedDelay({
            serviceScope.launch {
                val result = withContext(Dispatchers.IO) {
                    getPokemonCountDbUseCase()
                        .flatMap { count ->
                            offset = count
                            getPokemonListFromApiUseCase(limit, offset)!!
                        }
                        .flatMap { pokemonList ->
                            savePokemonInDbUseCase(pokemonList.map { it.toDomain() })
                        }
                }

                result.onSuccess {
                    offset += limit
                    limit = 10
                    showUpdateNotification()
                }.onFailure { ex ->
                    println(ex.message)
                    // TODO: Handle failure case, e.g., retry logic or user notification
                }
            }
        }, 0, 30, TimeUnit.SECONDS)
    }

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return START_STICKY
    }

    override fun onDestroy() {
        scheduler.shutdown()
        println("SERVICE DESTROYED")
        super.onDestroy()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val serviceChannel = NotificationChannel(
                CHANNEL_ID,
                "Pokemon Service Channel",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            val manager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(serviceChannel)
        }
    }

    private fun createInitialNotification(): Notification {
        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Pokemon Service")
            .setContentText("Fetching Pokemon data...")
            .setSmallIcon(R.drawable.ic_pokeball_notification)
            .build()
    }

    private fun showUpdateNotification() {
        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Pokemon List Updated")
            .setContentText("The list of Pokémon has been updated.")
            .setSmallIcon(R.drawable.ic_pokeball_notification)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .build()

        val manager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        manager.notify(2, notification)
    }

    companion object {
        const val CHANNEL_ID = "PokemonServiceChannel"
    }
}