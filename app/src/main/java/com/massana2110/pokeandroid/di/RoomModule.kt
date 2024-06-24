package com.massana2110.pokeandroid.di

import android.content.Context
import androidx.room.Room
import com.massana2110.pokeandroid.data.datasources.database.PokeAndroidDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RoomModule {

    @Provides
    @Singleton
    fun providePokemonDatabase(@ApplicationContext appContext: Context): PokeAndroidDatabase =
        Room.databaseBuilder(appContext, PokeAndroidDatabase::class.java, "PokeAndroidDatabase.db")
            .build()

    @Singleton
    @Provides
    fun providePokemonDao(db: PokeAndroidDatabase) = db.getPokemonDao()

    @Singleton
    @Provides
    fun providePokemonTypeDao(db: PokeAndroidDatabase) = db.getPokemonTypeDao()
}