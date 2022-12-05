package com.rasyidin.pokedexapp.util

import androidx.compose.ui.graphics.Color
import com.rasyidin.pokedexapp.data.response.Stat
import com.rasyidin.pokedexapp.data.response.StatsItem
import com.rasyidin.pokedexapp.data.response.Type
import com.rasyidin.pokedexapp.ui.theme.*

fun parseTypeColor(type: Type): Color {
    return when (type.name?.lowercase()) {
        "normal" -> Normal
        "fighting" -> Fighting
        "flying" -> Flying
        "poison" -> Poison
        "ground" -> Ground
        "rock" -> Rock
        "bug" -> Bug
        "ghost" -> Ghost
        "steel" -> Steel
        "fire" -> Fire
        "water" -> Water
        "electric" -> Electric
        "psychic" -> Psychic
        "ice" -> Ice
        "dragon" -> Dragon
        "fairy" -> Fairy
        "grass"-> Grass
        "shadow" -> Dark
        "dark" -> Dark
        else -> PaleBlue
    }
}

fun parseStat(stat: Stat): String {
    return when (stat.name?.lowercase()) {
        "hp" -> "HP"
        "attack" -> "ATK"
        "defense" -> "DEF"
        "special-attack" -> "SATK"
        "special-defense" -> "SDEF"
        "speed" -> "SPD"
        else -> ""
    }
}

fun downloadImagePokemon(number: Int) : String {
    return "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/dream-world/$number.svg"
}

fun downloadImagePokemon(number: String) : String {
    return "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/dream-world/$number.svg"
}