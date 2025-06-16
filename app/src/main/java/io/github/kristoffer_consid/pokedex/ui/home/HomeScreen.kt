package io.github.kristoffer_consid.pokedex.ui.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import co.pokeapi.pokekotlin.model.NamedApiResource
import coil3.ColorImage
import coil3.annotation.ExperimentalCoilApi
import coil3.compose.AsyncImage
import coil3.compose.AsyncImagePreviewHandler
import coil3.compose.LocalAsyncImagePreviewHandler
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootGraph
import io.github.kristoffer_consid.pokedex.R

@Destination<RootGraph>(start = true)
@Composable
fun HomeScreen() {
    val viewModel: HomeViewModel = hiltViewModel()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    if (uiState.isLoaded) {
        HomeDisplay(uiState)
    }
    else {
        LoadingScreen()
    }
}

@Composable
fun HomeDisplay(uiState: HomeUIState, modifier: Modifier = Modifier) {
    var query by remember { mutableStateOf("") }
    val filtered = remember(query) {
        if (query.isBlank()) {
            uiState.pokemonList
        }
        else {
            uiState.pokemonList.filter { it.name.contains(query, ignoreCase = true) }
        }
    }

    LazyVerticalGrid(
        columns = GridCells.Fixed(4),
        modifier = modifier
            .fillMaxSize()
            .padding(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        item(span = { GridItemSpan(maxCurrentLineSpan) }) {
            PokeHeader(uiState.randomPokemons)
        }

        stickyHeader {
            Surface(
                tonalElevation = 4.dp,
                modifier = Modifier.fillMaxWidth()
            ) {
                OutlinedTextField(
                    value = query,
                    onValueChange = { query = it },
                    placeholder = { Text("Search…") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            bottom = 8.dp,
                            top = 8.dp
                        )
                )
            }
        }

        items(filtered, key = { it.id }) { pokemon ->
            GridCell(
                pokemon,
                Modifier.padding(2.dp)
            )
        }
    }
}

@Composable
fun PokeHeader(
    randomPokemons: Triple<NamedApiResource, NamedApiResource, NamedApiResource>?,
    modifier: Modifier = Modifier
) {
    if (randomPokemons == null) {
        return
    }

    Row(
        modifier = modifier
            .height(200.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        RandomPokemon(
            randomPokemons.first,
            Modifier
                .fillMaxWidth()
                .weight(1f).padding(
                    bottom = 16.dp
                )
        )

        RandomPokemon(
            randomPokemons.second,
            Modifier.fillMaxWidth().weight(1f)
        )

        RandomPokemon(
            randomPokemons.third,
            Modifier
                .fillMaxWidth()
                .weight(1f).padding(
                    bottom = 16.dp
                )
        )
    }
}

@Composable
fun RandomPokemon(pokemon: NamedApiResource, modifier: Modifier = Modifier) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        // TODO: get LocalAsyncImagePreviewHandler to work so we won't need this
        if (LocalInspectionMode.current) {
            Image(
                painter = painterResource(R.drawable.pokeball),
                contentDescription = pokemon.name,
            )
        }
        else {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data("https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/${pokemon.id}.png")
                    .crossfade(true)
                    .build(),
                placeholder = painterResource(R.drawable.pokeball),
                contentDescription = pokemon.name,
            )
        }

        Text(
            pokemon.name.replaceFirstChar { it.uppercase() },
            style = MaterialTheme.typography.titleLarge
        )
    }

}

@OptIn(ExperimentalCoilApi::class)
@Preview(showBackground = true)
@Composable
fun PokeHeaderPreview() {
    CompositionLocalProvider(LocalAsyncImagePreviewHandler provides previewHandler) {
        PokeHeader(
            Triple(
                NamedApiResource("Preview", "", 1),
                NamedApiResource("Preview", "", 2),
                NamedApiResource("Preview", "", 3)
            ),
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
fun GridCell(pokemon: NamedApiResource, modifier: Modifier = Modifier) {
    Column(modifier = modifier) {
        Text(
            "#${pokemon.id}",
            style = MaterialTheme.typography.titleMedium
        )

        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // TODO: get LocalAsyncImagePreviewHandler to work so we won't need this
            if (LocalInspectionMode.current) {
                Image(
                    painter = painterResource(R.drawable.pokeball),
                    contentDescription = pokemon.name,
                )
            }
            else {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data("https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/${pokemon.id}.png")
                        .crossfade(true)
                        .build(),
                    placeholder = painterResource(R.drawable.pokeball),
                    contentDescription = pokemon.name
                )
            }

            Text(
                pokemon.name.replaceFirstChar { it.uppercase() },
                style = MaterialTheme.typography.titleMedium,
            )
        }
    }
}

@OptIn(ExperimentalCoilApi::class)
@Preview(showBackground = true)
@Composable
fun GridCellPreview() {
    CompositionLocalProvider(LocalAsyncImagePreviewHandler provides previewHandler) {
        GridCell(
            NamedApiResource("Preview", "", 1),
            modifier = Modifier.fillMaxWidth()
        )
    }
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

@OptIn(ExperimentalCoilApi::class)
val previewHandler = AsyncImagePreviewHandler {
    ColorImage(Color.Red.toArgb())
}