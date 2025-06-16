package io.github.kristoffer_consid.pokedex.ui.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.pokeapi.pokekotlin.PokeApi
import co.pokeapi.pokekotlin.model.NamedApiResource
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel(assistedFactory = DetailsViewModel.DetailsViewModelFactory::class)
class DetailsViewModel @AssistedInject constructor(
    @Assisted val pokemonInfo: NamedApiResource
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
    }

    fun loadPokemonDetailedData() {
        viewModelScope.launch {
            val result = PokeApi.getPokemonSpecies(viewModelState.value.pokemonInfo.id)

            viewModelState.update {
                it.processResult(ResultType.SPECIES, result)
            }
        }
    }

    @AssistedFactory
    interface DetailsViewModelFactory {
        fun create(pokemonInfo: NamedApiResource): DetailsViewModel
    }
}