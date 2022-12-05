package com.rasyidin.pokedexapp.ui.screen.detail

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rasyidin.pokedexapp.data.repository.PokemonRepository
import com.rasyidin.pokedexapp.data.response.Pokemon
import com.rasyidin.pokedexapp.util.ResultState
import com.rasyidin.pokedexapp.util.onFailure
import com.rasyidin.pokedexapp.util.onLoading
import com.rasyidin.pokedexapp.util.onSuccess
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(private val repository: PokemonRepository) : ViewModel() {

    private var _pokemonUiState: MutableStateFlow<ResultState<Pokemon>> =
        MutableStateFlow(ResultState.Loading())
    val pokemon get() = _pokemonUiState.asStateFlow()

    private var _isFavorite = mutableStateOf(false)
    val isFavorite: State<Boolean> get() = _isFavorite

    fun getDetailPokemon(pokemonName: String) {
        viewModelScope.launch {
            repository.filterPokemonByNameOrId(pokemonName).collect { result ->
                result.onSuccess { pokemon ->
                    pokemon?.let {
                        _isFavorite.value = repository.filterFavStatePokemonById(pokemon.id!!)
                        _pokemonUiState.value = ResultState.Success(pokemon)
                    }
                }

                result.onLoading {
                    _pokemonUiState.value = ResultState.Loading()
                }

                result.onFailure { message ->
                    _pokemonUiState.value = ResultState.Error(message)
                }
            }
        }
    }

    fun addFavPokemon(pokemon: Pokemon) {
        repository.addFavPokemon(pokemon)
        _isFavorite.value = repository.filterFavStatePokemonById(pokemon.id!!)
    }

    fun removeFavPokemon(pokemon: Pokemon) {
        repository.removeFavPokemon(pokemon)
        _isFavorite.value = repository.filterFavStatePokemonById(pokemon.id!!)
    }

}