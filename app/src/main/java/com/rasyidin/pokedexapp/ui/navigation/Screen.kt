package com.rasyidin.pokedexapp.ui.navigation

import androidx.compose.ui.graphics.Color

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Detail : Screen("detail/{pokemonName}/{pokemonDominantColor}") {
        const val POKEMON_NAME = "pokemonName"
        const val POKEMON_DOMINANT_COLOR = "pokemonDominantColor"
        fun createRoute(pokemonName: String, dominantColor: Int) = "detail/$pokemonName/$dominantColor"
    }
    object Profile : Screen("profile")
}
