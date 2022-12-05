package com.rasyidin.pokedexapp.ui.screen.profile

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.palette.graphics.Palette
import com.rasyidin.pokedexapp.data.repository.PokemonRepository
import com.rasyidin.pokedexapp.data.response.Pokemon
import com.rasyidin.pokedexapp.di.AppModule
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val repository: PokemonRepository
) : ViewModel() {

    private var _favListPokemon = MutableStateFlow(AppModule.favListPokemon)
    val favListPokemon get() = _favListPokemon.asStateFlow()

    init {
        getFavPokemon()
    }

    fun getFavPokemon() {
        _favListPokemon.value = repository.getFavPokemon().toMutableList()
    }

    fun findDominantColor(drawable: Drawable, onFinish: (Color) -> Unit) {
        val bitmap = (drawable as BitmapDrawable).bitmap.copy(
            Bitmap.Config.ARGB_8888, true
        )
        Palette.from(bitmap).generate { palette ->
            palette?.dominantSwatch?.rgb?.let { colorValue ->
                onFinish(Color(colorValue))
            }
        }
    }
}