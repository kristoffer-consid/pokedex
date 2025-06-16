package io.github.kristoffer_consid.pokedex.ui.details

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import co.pokeapi.pokekotlin.model.NamedApiResource
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootGraph


@Destination<RootGraph>
@Composable
fun DetailsScreen(pokemonInfo: NamedApiResource) {
    val viewModel = hiltViewModel<DetailsViewModel, DetailsViewModel.DetailsViewModelFactory> { factory ->
        factory.create(pokemonInfo)
    }

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()


    if (uiState.isLoading) {
        Text("Loading")
    }
    else {
        Text(uiState.species?.name ?: "null")
    }
}