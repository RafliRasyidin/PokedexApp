package com.rasyidin.pokedexapp.data.network

import com.rasyidin.pokedexapp.data.response.Pokemon
import com.rasyidin.pokedexapp.data.response.PokemonList
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface PokeApi {

    @GET("pokemon")
    suspend fun getPokemonList(
        @Query("limit") limit: Int,
        @Query("offset") offset: Int
    ): Response<PokemonList>

    @GET("pokemon/{name}")
    suspend fun getPokemonDetail(
        @Path("name") name: String
    ): Response<Pokemon>
}