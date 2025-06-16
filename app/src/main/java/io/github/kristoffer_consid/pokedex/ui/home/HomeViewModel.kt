package io.github.kristoffer_consid.pokedex.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.pokeapi.pokekotlin.PokeApi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(): ViewModel() {
    private val viewModelState = MutableStateFlow(HomeUIState())
    val uiState = viewModelState.stateIn(
        viewModelScope,
        SharingStarted.Eagerly,
        viewModelState.value
    )

    init {
        loadPokemonList()
    }

    fun refreshRandomPokemon() {
        val pokemonList = viewModelState.value.pokemonList
        if (pokemonList.isEmpty()) {
            return
        }

        viewModelState.update {
            it.copy(
                randomPokemons = Triple(
                    pokemonList.random(),
                    pokemonList.random(),
                    pokemonList.random()
                )
            )
        }
    }

    private fun loadPokemonList() {
        viewModelScope.launch {
            //  The response object is about 75 kB (at the time of writing) and a non-cached
            //  response takes about 34 ms so there's no real need to use pagination here
            val result = PokeApi.getPokemonSpeciesList(0, 10000)

            viewModelState.update {
                it.processResult(ResultType.POKEMON_LIST, result)
            }

            refreshRandomPokemon()
        }
    }
}