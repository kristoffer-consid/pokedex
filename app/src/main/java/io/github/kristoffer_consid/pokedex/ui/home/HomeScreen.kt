package io.github.kristoffer_consid.pokedex.ui.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.repeatOnLifecycle
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
import com.ramcosta.composedestinations.generated.destinations.DetailsScreenDestination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import io.github.kristoffer_consid.pokedex.R
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.apache.commons.text.similarity.JaroWinklerSimilarity

const val FUZZY_THRESHOLD = 0.8

@Destination<RootGraph>(start = true)
@Composable
fun HomeScreen(navigator: DestinationsNavigator) {
    val viewModel: HomeViewModel = hiltViewModel()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val lifecycleOwner = LocalLifecycleOwner.current
    LaunchedEffect(lifecycleOwner) {
        lifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
            viewModel.loadFavorites()
        }
    }

    if (uiState.isLoaded) {
        HomeDisplay(
            uiState,
            onRefreshRandomPokemon = { viewModel.refreshRandomPokemon() }
        ) { pokemonInfo ->
            navigator.navigate(DetailsScreenDestination(pokemonInfo))
        }
    } else {
        LoadingScreen()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeDisplay(
    uiState: HomeUIState,
    modifier: Modifier = Modifier,
    onRefreshRandomPokemon: () -> Unit = {},
    onClick: (NamedApiResource) -> Unit = {}
) {
    var isRefreshing by remember { mutableStateOf(false) }
    val refreshingScope = rememberCoroutineScope()
    var query by remember { mutableStateOf("") }
    var showFavorites by remember { mutableStateOf(false) }
    var pokemonList = uiState.pokemonList.filter {
        !showFavorites || uiState.favorites.contains(it.id)
    }

    val filtered = remember(query, showFavorites) {
        if (query.isBlank()) {
            pokemonList
        } else {
            val similarity = JaroWinklerSimilarity()
            val lowercaseQuery = query.lowercase()

            // Fuzzy search pokemon name with Jaro-Winkler
            pokemonList
                .map { pokemon ->
                    pokemon to similarity.apply(pokemon.name.lowercase(), lowercaseQuery)
                }
                .filter {
                    it.first.name.lowercase()
                        .contains(lowercaseQuery) || it.second >= FUZZY_THRESHOLD
                }
                .sortedByDescending { it.second }
                .map { it.first }
        }
    }

    PullToRefreshBox(
        isRefreshing = isRefreshing,
        onRefresh = {
            refreshingScope.launch {
                isRefreshing = true
                onRefreshRandomPokemon()
                delay(250L)
                isRefreshing = false
            }
        },
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.primary)
    ) {
        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.padding(
                start = 8.dp,
                end = 8.dp
            )
        ) {
            item(span = { GridItemSpan(maxCurrentLineSpan) }) {
                PokeHeader(uiState.randomPokemons, onClick = onClick)
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
                        trailingIcon = {
                            IconToggleButton(
                                checked = showFavorites,
                                onCheckedChange = { showFavorites = it }
                            ) {
                                if (showFavorites) {
                                    Icon(
                                        Icons.Filled.Favorite,
                                        contentDescription = "Favourite",
                                        tint = MaterialTheme.colorScheme.onPrimary
                                    )
                                }
                                else {
                                    Icon(
                                        Icons.Outlined.FavoriteBorder,
                                        contentDescription = "Not favourite",
                                        tint = MaterialTheme.colorScheme.onPrimary
                                    )
                                }
                            }
                        },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = MaterialTheme.colorScheme.onPrimary,
                            unfocusedBorderColor = MaterialTheme.colorScheme.onPrimary,
                            focusedTextColor = MaterialTheme.colorScheme.onPrimary,
                            unfocusedTextColor = MaterialTheme.colorScheme.onPrimary,
                            focusedPlaceholderColor = MaterialTheme.colorScheme.onPrimary,
                            unfocusedPlaceholderColor = MaterialTheme.colorScheme.onPrimary,
                        ),
                        modifier = Modifier
                            .background(MaterialTheme.colorScheme.primary)
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
                    Modifier.padding(2.dp),
                    onClick = onClick
                )
            }
        }
    }
}

@Composable
fun PokeHeader(
    randomPokemons: Triple<NamedApiResource, NamedApiResource, NamedApiResource>?,
    modifier: Modifier = Modifier,
    onClick: (NamedApiResource) -> Unit = {}
) {
    if (randomPokemons == null) {
        return
    }

    Row(
        modifier = modifier
            .height(200.dp)
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(Color.White),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        RandomPokemon(
            randomPokemons.first,
            Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(
                    bottom = 24.dp
                ),
            onClick = onClick
        )

        RandomPokemon(
            randomPokemons.second,
            Modifier
                .fillMaxWidth()
                .weight(1f),
            onClick = onClick
        )

        RandomPokemon(
            randomPokemons.third,
            Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(
                    bottom = 24.dp
                ),
            onClick = onClick
        )
    }
}

@Composable
fun RandomPokemon(
    pokemon: NamedApiResource,
    modifier: Modifier = Modifier,
    onClick: (NamedApiResource) -> Unit = {}
) {
    Surface(
        onClick = { onClick(pokemon) },
        modifier = modifier
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            // TODO: get LocalAsyncImagePreviewHandler to work so we won't need this
            if (LocalInspectionMode.current) {
                Image(
                    painter = painterResource(R.drawable.pokeball),
                    contentDescription = pokemon.name,
                )
            } else {
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
fun GridCell(
    pokemon: NamedApiResource,
    modifier: Modifier = Modifier,
    onClick: (NamedApiResource) -> Unit = {}
) {
    Surface(
        modifier = modifier
            .background(MaterialTheme.colorScheme.primary),
        onClick = { onClick(pokemon) }
    ) {
        Column(modifier = Modifier.background(MaterialTheme.colorScheme.primary)) {
            Text(
                "#${pokemon.id}",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.background(MaterialTheme.colorScheme.primary),
                color = MaterialTheme.colorScheme.onPrimary
            )

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.background(MaterialTheme.colorScheme.primary)
            ) {
                // TODO: get LocalAsyncImagePreviewHandler to work so we won't need this
                if (LocalInspectionMode.current) {
                    Image(
                        painter = painterResource(R.drawable.pokeball),
                        contentDescription = pokemon.name,
                    )
                } else {
                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data("https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/${pokemon.id}.png")
                            .crossfade(true)
                            .build(),
                        placeholder = painterResource(R.drawable.pokeball),
                        contentDescription = pokemon.name,
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.primaryContainer)
                    )
                }

                Text(
                    pokemon.name.replaceFirstChar { it.uppercase() },
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.background(MaterialTheme.colorScheme.primary),
                    color = MaterialTheme.colorScheme.onPrimary
                )
            }
        }
    }
}

@OptIn(ExperimentalCoilApi::class)
@Preview(showBackground = true, widthDp = 100)
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