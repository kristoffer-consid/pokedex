package io.github.kristoffer_consid.pokedex.ui.details

import co.pokeapi.pokekotlin.model.EvolutionChain
import co.pokeapi.pokekotlin.model.NamedApiResource
import co.pokeapi.pokekotlin.model.PokemonSpecies

enum class ResultType {
    SPECIES,
    EVOLUTION_CHAIN,
}

data class DetailsUiState(
    val pokemonInfo: NamedApiResource,
    val isLoading: Boolean = true,

    val species: PokemonSpecies? = null,
    val evolutionChain: EvolutionChain? = null,

    val errorMessages: List<String> = emptyList(),
) {
    fun processResult(type: ResultType, result: Result<Any>) = result.fold(
        onSuccess = { data ->
            when (type) {
                ResultType.SPECIES -> this.copy(
                    species = data as PokemonSpecies
                )

                ResultType.EVOLUTION_CHAIN -> this.copy(
                    evolutionChain = data as EvolutionChain
                )
            }
        },
        onFailure = { exception ->
            val error = exception.message ?: ""
            this.copy(errorMessages = this.errorMessages + error)
        }
    )
}