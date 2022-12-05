package com.rasyidin.pokedexapp.di

import com.rasyidin.pokedexapp.data.network.PokeApi
import com.rasyidin.pokedexapp.data.repository.PokemonRepository
import com.rasyidin.pokedexapp.data.repository.PokemonRepositoryImpl
import com.rasyidin.pokedexapp.data.response.Pokemon
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    companion object {
        const val BASE_URL = "https://pokeapi.co/api/v2/"

        val favListPokemon = mutableListOf<Pokemon>()
    }

    @Provides
    @Singleton
    fun providesPokeApi(): PokeApi =
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(PokeApi::class.java)

    @Provides
    @Singleton
    fun providesPokemonRepository(api: PokeApi): PokemonRepository = PokemonRepositoryImpl(api)


}