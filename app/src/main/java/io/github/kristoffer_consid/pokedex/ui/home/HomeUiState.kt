package io.github.kristoffer_consid.pokedex.ui.home

import co.pokeapi.pokekotlin.model.NamedApiResource
import co.pokeapi.pokekotlin.model.NamedApiResourceList

enum class ResultType {
    POKEMON_LIST
}

data class HomeUIState(
    val isLoaded: Boolean = false,
    val pokemonList: List<NamedApiResource> = emptyList(),
    val randomPokemons: Triple<NamedApiResource, NamedApiResource, NamedApiResource>? = null,

    val errorMessages: List<String> = emptyList()
) {
    fun processResult(type: ResultType, result: Result<Any>) = result.fold(
        onSuccess = { data ->
            when (type) {
                ResultType.POKEMON_LIST -> this.copy(
                    isLoaded = true,
                    pokemonList = (data as NamedApiResourceList).results
                )
            }
        },
        onFailure = { exception ->
            val error = exception.message ?: ""
            this.copy(errorMessages = this.errorMessages + error)
        }
    )
}