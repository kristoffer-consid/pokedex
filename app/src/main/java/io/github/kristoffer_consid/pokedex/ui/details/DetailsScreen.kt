package io.github.kristoffer_consid.pokedex.ui.details

import android.widget.ToggleButton
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import co.pokeapi.pokekotlin.model.ApiResource
import co.pokeapi.pokekotlin.model.EvolutionChain
import co.pokeapi.pokekotlin.model.EvolutionTrigger
import co.pokeapi.pokekotlin.model.NamedApiResource
import co.pokeapi.pokekotlin.model.PokemonSpecies
import co.pokeapi.pokekotlin.model.PokemonSpeciesFlavorText
import coil3.ColorImage
import coil3.annotation.ExperimentalCoilApi
import coil3.compose.AsyncImage
import coil3.compose.AsyncImagePreviewHandler
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootGraph
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import io.github.kristoffer_consid.pokedex.R
import io.github.kristoffer_consid.pokedex.domain.PreviewSpecies


@OptIn(ExperimentalMaterial3Api::class)
@Destination<RootGraph>
@Composable
fun DetailsScreen(navigator: DestinationsNavigator, pokemonInfo: NamedApiResource) {
    val viewModel = hiltViewModel<DetailsViewModel, DetailsViewModel.DetailsViewModelFactory> { factory ->
        factory.create(pokemonInfo)
    }

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            AppBar(
                title = uiState.pokemonInfo.name.replaceFirstChar { it.uppercase() },
                onBackClick = { navigator.popBackStack() },
                actions = {
                    IconToggleButton(
                        checked = false,
                        onCheckedChange = { checked -> {} }
                    ) {
                        Icon(
                            Icons.Outlined.FavoriteBorder,
                            contentDescription = "Favourite",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            )
        }
    ) { contentPadding ->
        if (uiState.isLoading) {
            Text("Loading")
        }
        else {
            Column(modifier = Modifier.padding(contentPadding)) {
                Header(uiState.pokemonInfo)
            }
        }
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppBar(
    title: String,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
    scrollBehavior: TopAppBarScrollBehavior? = null,
    actions: @Composable RowScope.() -> Unit = {}
) {
    CenterAlignedTopAppBar(
        modifier = modifier,
        actions = actions,
        title = { Text(text = title) },
        scrollBehavior = scrollBehavior,
        windowInsets = WindowInsets(0.dp),
        navigationIcon = {
            IconButton(onClick = onBackClick) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Localized description"
                )
            }
        }
    )
}

@Composable
fun Header(pokemonInfo: NamedApiResource, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(250.dp),
        contentAlignment = Alignment.Center
    ) {
        if (LocalInspectionMode.current) {
            Image(
                painter = painterResource(R.drawable.pokeball),
                contentScale = ContentScale.FillHeight,
                contentDescription = pokemonInfo.name,
            )
        } else {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data("https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/${pokemonInfo.id}.png")
                    .crossfade(true)
                    .build(),
                contentScale = ContentScale.FillHeight,
                placeholder = painterResource(R.drawable.pokeball),
                contentDescription = pokemonInfo.name,
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HeaderPreview() {
    Header(NamedApiResource("Preview", "", 1))
}

@Composable
fun EvolutionCard(evolutionChain: EvolutionChain?) {
    if (evolutionChain?.chain?.species == null) {
        return@EvolutionCard
    }

    Card {
        Column {
            Text(evolutionChain.chain.species.name)

            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                EvolutionPart(evolutionChain.chain.species)

                Column {
                    evolutionChain.chain.evolvesTo.map {
                        // EvolutionPart(it.species, it.evolutionDetails)
                    }
                }

            }
        }
    }
}

@Composable
fun EvolutionPart(
    pokemon: NamedApiResource,
    trigger: EvolutionTrigger? = null,
    modifier: Modifier = Modifier
) {

}

@Preview(showBackground = true)
@Composable
fun EvolutionCardPreview() {
    EvolutionCard(null)
}

@Composable
fun LoadingScreen() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        if (LocalInspectionMode.current) {
            CircularProgressIndicator(
                progress = { 0.7f },
            )
        } else {
            CircularProgressIndicator()
        }

    }
}

@Preview(showBackground = true)
@Composable
fun LoadingScreenPreview() {
    LoadingScreen()
}

