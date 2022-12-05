package com.rasyidin.pokedexapp.data.repository

import com.rasyidin.pokedexapp.data.network.PokeApi
import com.rasyidin.pokedexapp.data.response.Pokemon
import com.rasyidin.pokedexapp.data.response.PokemonList
import com.rasyidin.pokedexapp.di.AppModule
import com.rasyidin.pokedexapp.util.ResultState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class PokemonRepositoryImpl @Inject constructor(private val api: PokeApi) : PokemonRepository {

    override fun getPokemonList(limit: Int, offset: Int): Flow<ResultState<PokemonList>> {
        return flow {
            emit(ResultState.Loading())
            val response = api.getPokemonList(limit, offset)
            if (response.isSuccessful) {
                emit(ResultState.Success(response.body()))
            } else {
                emit(ResultState.Error(response.message()))
            }
        }.catch { error ->
            emit(ResultState.Error(error.message.toString()))
        }.flowOn(Dispatchers.IO)
    }

    override fun filterPokemonByNameOrId(name: String): Flow<ResultState<Pokemon>> {
        return flow {
            emit(ResultState.Loading())
            val response = api.getPokemonDetail(name)
            if (response.isSuccessful) {
                emit(ResultState.Success(response.body()))
            } else {
                emit(ResultState.Error(response.message()))
            }
        }.catch { error ->
            emit(ResultState.Error(error.message.toString()))
        }.flowOn(Dispatchers.IO)
    }

    override fun filterFavStatePokemonById(id: Int): Boolean {
        if (AppModule.favListPokemon.isNotEmpty()) {
            AppModule.favListPokemon.forEach {
                if (it.id == id) return true
            }
        } else {
            return false
        }
        return false
    }

    override fun getFavPokemon(): List<Pokemon> {
        return AppModule.favListPokemon
    }

    override fun addFavPokemon(pokemon: Pokemon) {
        AppModule.favListPokemon.add(pokemon)
    }

    override fun removeFavPokemon(pokemon: Pokemon) {
        AppModule.favListPokemon.remove(pokemon)
    }
}