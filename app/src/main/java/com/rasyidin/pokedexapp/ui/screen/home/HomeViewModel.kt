package com.rasyidin.pokedexapp.ui.screen.home

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.capitalize
import androidx.compose.ui.text.intl.Locale
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.palette.graphics.Palette
import com.rasyidin.pokedexapp.data.repository.PokemonRepository
import com.rasyidin.pokedexapp.model.PokedexUi
import com.rasyidin.pokedexapp.util.downloadImagePokemon
import com.rasyidin.pokedexapp.util.onFailure
import com.rasyidin.pokedexapp.util.onLoading
import com.rasyidin.pokedexapp.util.onSuccess
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val repository: PokemonRepository): ViewModel() {

    private var curPage = 0

    private var _pokedexUiState: MutableStateFlow<List<PokedexUi>> = MutableStateFlow(listOf())
    val pokedexUiState get() = _pokedexUiState.asStateFlow()

    var endReached = mutableStateOf(false)

    private var _query = mutableStateOf("")
    val query: State<String> get() = _query

    private var cachedPokemonList = listOf<PokedexUi>()
    private var isSearchStarting = true
    var isSearching = mutableStateOf(false)

    private var _isLoading = mutableStateOf(false)
    val isLoading: State<Boolean> get() = _isLoading

    init {
        getPokemonList()
    }

    fun searchPokemon(searchQuery: String) {
        _query.value = searchQuery
        val listToSearch = if (isSearchStarting) {
            pokedexUiState.value
        } else cachedPokemonList
        viewModelScope.launch(Dispatchers.Default) {
            if (searchQuery.isEmpty()) {
                _pokedexUiState.value = cachedPokemonList
                isSearching.value = false
                isSearchStarting = true
                return@launch
            }
            val results = listToSearch.filter {
                it.name!!.contains(searchQuery.trim(), ignoreCase = true) || it.index.toString() == searchQuery.trim()
            }
            if (isSearchStarting) {
                cachedPokemonList = _pokedexUiState.value
                isSearchStarting = false
            }
            _pokedexUiState.value = results
            isSearching.value = true
        }
    }

    fun getPokemonList(limit: Int = 20, offset: Int = 20) {
        viewModelScope.launch {
            repository.getPokemonList(limit, curPage * offset).collect { result ->
                result.onSuccess { pokemonList ->
                    endReached.value = curPage * 20 >= (pokemonList?.count ?: 0)
                    val pokeDexEntries = pokemonList?.results?.mapIndexed { index, resultsItem ->
                        val number = if (resultsItem.url!!.endsWith("/")) {
                            resultsItem.url.dropLast(1).takeLastWhile { it.isDigit() }
                        } else resultsItem.url.takeLastWhile { it.isDigit() }
                        val url = downloadImagePokemon(number)
                        PokedexUi(
                            name = resultsItem.name?.capitalize(Locale.current).toString(),
                            imageUrl = url,
                            index = number.toInt()
                        )
                    }
                    curPage++
                    _pokedexUiState.value += pokeDexEntries ?: emptyList()
                    _isLoading.value = false
                }

                result.onFailure { message ->
                    _isLoading.value = false
                }

                result.onLoading {
                    _isLoading.value = true
                }
            }
        }
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