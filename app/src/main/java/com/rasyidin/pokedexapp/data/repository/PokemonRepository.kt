package com.rasyidin.pokedexapp.data.repository

import com.rasyidin.pokedexapp.data.response.Pokemon
import com.rasyidin.pokedexapp.data.response.PokemonList
import com.rasyidin.pokedexapp.util.ResultState
import kotlinx.coroutines.flow.Flow

interface PokemonRepository {

    fun getPokemonList(
        limit: Int,
        offset: Int
    ): Flow<ResultState<PokemonList>>

    fun filterPokemonByNameOrId(name: String): Flow<ResultState<Pokemon>>

    fun filterFavStatePokemonById(id: Int): Boolean

    fun getFavPokemon(): List<Pokemon>

    fun addFavPokemon(pokemon: Pokemon)

    fun removeFavPokemon(pokemon: Pokemon)
}
