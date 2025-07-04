package io.github.kristoffer_consid.pokedex.ui.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.pokeapi.pokekotlin.PokeApi
import co.pokeapi.pokekotlin.model.NamedApiResource
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.kristoffer_consid.pokedex.data.pokemon.FavoritesRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel(assistedFactory = DetailsViewModel.DetailsViewModelFactory::class)
class DetailsViewModel @AssistedInject constructor(
    @Assisted val pokemonInfo: NamedApiResource,
    private val favoritesRepository: FavoritesRepository
): ViewModel() {
    private val viewModelState = MutableStateFlow(DetailsUiState(
        pokemonInfo = pokemonInfo
    ))

    val uiState = viewModelState.stateIn(
        viewModelScope,
        SharingStarted.Eagerly,
        viewModelState.value
    )

    init {
        loadPokemonDetailedData()
        loadFavorites()
    }

    fun toggleFavorite() {
        val pokemonId = viewModelState.value.pokemonInfo.id

        viewModelScope.launch {
            val result = favoritesRepository.toggleFavorite(pokemonId)
            viewModelState.update { it.processResult(ResultType.FAVORITES, result) }
        }
    }

    private fun loadPokemonDetailedData() {
        viewModelScope.launch {
            // Get species details
            val result = PokeApi.getPokemonSpecies(viewModelState.value.pokemonInfo.id)
            viewModelState.update {
                it.processResult(ResultType.SPECIES, result)
            }

            // Get evolution chain
            val evolutionId = result.getOrNull()?.evolutionChain?.id
            if (evolutionId !== null) {
                val result = PokeApi.getEvolutionChain(evolutionId)
                viewModelState.update {
                    it.processResult(ResultType.EVOLUTION_CHAIN, result)
                }
            }

            // Finished loading
            viewModelState.update {
                it.copy(
                    isLoading = false
                )
            }
        }
    }

    private fun loadFavorites() {
        viewModelScope.launch {
            val result = favoritesRepository.getFavorites()
            viewModelState.update { it.processResult(ResultType.FAVORITES, result) }
        }
    }

    @AssistedFactory
    interface DetailsViewModelFactory {
        fun create(pokemonInfo: NamedApiResource): DetailsViewModel
    }
}