package com.rasyidin.pokedexapp.data.response

import com.google.gson.annotations.SerializedName

data class Pokemon(

    @field:SerializedName("types")
    val types: List<TypesItem?>? = null,

    @field:SerializedName("base_experience")
    val baseExperience: Int? = null,

    @field:SerializedName("weight")
    val weight: Int? = null,

    @field:SerializedName("sprites")
    val sprites: Sprites? = null,

    @field:SerializedName("species")
    val species: Species? = null,

    @field:SerializedName("stats")
    val stats: List<StatsItem>? = null,

    @field:SerializedName("name")
    val name: String? = null,

    @field:SerializedName("id")
    val id: Int? = null,

    @field:SerializedName("height")
    val height: Int? = null,

    var isFavorite: Boolean = false
)

data class DreamWorld(

    @field:SerializedName("front_default")
    val frontDefault: String? = null,

    @field:SerializedName("front_female")
    val frontFemale: Any? = null
)

data class Other(

    @field:SerializedName("dream_world")
    val dreamWorld: DreamWorld? = null,

    @field:SerializedName("official-artwork")
    val officialArtwork: OfficialArtwork? = null,

    @field:SerializedName("home")
    val home: Home? = null
)

data class Home(

    @field:SerializedName("front_shiny_female")
    val frontShinyFemale: Any? = null,

    @field:SerializedName("front_default")
    val frontDefault: String? = null,

    @field:SerializedName("front_female")
    val frontFemale: Any? = null,

    @field:SerializedName("front_shiny")
    val frontShiny: String? = null
)

data class Sprites(

    @field:SerializedName("back_shiny_female")
    val backShinyFemale: Any? = null,

    @field:SerializedName("back_female")
    val backFemale: Any? = null,

    @field:SerializedName("other")
    val other: Other? = null,

    @field:SerializedName("back_default")
    val backDefault: String? = null,

    @field:SerializedName("front_shiny_female")
    val frontShinyFemale: Any? = null,

    @field:SerializedName("front_default")
    val frontDefault: String? = null,

    @field:SerializedName("front_female")
    val frontFemale: Any? = null,

    @field:SerializedName("back_shiny")
    val backShiny: String? = null,

    @field:SerializedName("front_shiny")
    val frontShiny: String? = null
)

data class OfficialArtwork(

    @field:SerializedName("front_default")
    val frontDefault: String? = null
)

data class Ability(

    @field:SerializedName("name")
    val name: String? = null,

    @field:SerializedName("url")
    val url: String? = null
)

data class Type(

    @field:SerializedName("name")
    val name: String? = null,

    @field:SerializedName("url")
    val url: String? = null
)

data class Stat(

    @field:SerializedName("name")
    val name: String? = null,

    @field:SerializedName("url")
    val url: String? = null
)

data class TypesItem(

    @field:SerializedName("slot")
    val slot: Int? = null,

    @field:SerializedName("type")
    val type: Type? = null
)

data class StatsItem(

    @field:SerializedName("stat")
    val stat: Stat? = null,

    @field:SerializedName("base_stat")
    val baseStat: Int? = null,

    @field:SerializedName("effort")
    val effort: Int? = null
)

data class Species(

    @field:SerializedName("name")
    val name: String? = null,

    @field:SerializedName("url")
    val url: String? = null
)