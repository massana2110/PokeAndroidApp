package com.massana2110.pokeandroid.di

import com.massana2110.pokeandroid.data.datasources.network.PokeAndroidClient
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    private const val BASE_API_URL = "https://pokeapi.co/api/v2/"

    @Singleton
    @Provides
    fun provideRetrofit(): Retrofit = Retrofit.Builder()
        .baseUrl(BASE_API_URL)
        .addConverterFactory(MoshiConverterFactory.create())
        .build()

    @Singleton
    @Provides
    fun providePokeAndroidClient(retrofit: Retrofit): PokeAndroidClient =
        retrofit.create(PokeAndroidClient::class.java)
}