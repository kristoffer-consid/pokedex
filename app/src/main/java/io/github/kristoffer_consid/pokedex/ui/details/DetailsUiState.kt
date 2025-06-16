package io.github.kristoffer_consid.pokedex.ui.details

import co.pokeapi.pokekotlin.model.NamedApiResource
import co.pokeapi.pokekotlin.model.PokemonSpecies

enum class ResultType {
    SPECIES
}

data class DetailsUiState(
    val pokemonInfo: NamedApiResource,
    val isLoading: Boolean = true,
    val species: PokemonSpecies? = null,

    val errorMessages: List<String> = emptyList(),
) {
    fun processResult(type: ResultType, result: Result<Any>) = result.fold(
        onSuccess = { data ->
            when (type) {
                ResultType.SPECIES -> this.copy(
                    isLoading = false,
                    species = data as PokemonSpecies
                )
            }
        },
        onFailure = { exception ->
            val error = exception.message ?: ""
            this.copy(errorMessages = this.errorMessages + error)
        }
    )
}