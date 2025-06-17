package io.github.kristoffer_consid.pokedex.ui.home

import co.pokeapi.pokekotlin.model.NamedApiResource
import co.pokeapi.pokekotlin.model.NamedApiResourceList

enum class ResultType {
    POKEMON_LIST,
    FAVORITES
}

data class HomeUIState(
    val isLoaded: Boolean = false,
    val pokemonList: List<NamedApiResource> = emptyList(),
    val randomPokemons: Triple<NamedApiResource, NamedApiResource, NamedApiResource>? = null,

    val favorites: List<Int> = emptyList(),
    val errorMessages: List<String> = emptyList()
) {
    @Suppress("UNCHECKED_CAST")
    fun processResult(type: ResultType, result: Result<Any>) = result.fold(
        onSuccess = { data ->
            when (type) {
                ResultType.POKEMON_LIST -> this.copy(
                    isLoaded = true,
                    pokemonList = (data as NamedApiResourceList).results
                )

                ResultType.FAVORITES -> this.copy(
                    favorites =  data as List<Int>
                )
            }
        },
        onFailure = { exception ->
            val error = exception.message ?: ""
            this.copy(errorMessages = this.errorMessages + error)
        }
    )
}