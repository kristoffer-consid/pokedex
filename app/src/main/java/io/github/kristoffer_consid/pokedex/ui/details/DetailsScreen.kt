package io.github.kristoffer_consid.pokedex.ui.details

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import co.pokeapi.pokekotlin.model.ChainLink
import co.pokeapi.pokekotlin.model.EvolutionChain
import co.pokeapi.pokekotlin.model.NamedApiResource
import co.pokeapi.pokekotlin.model.PokemonSpecies
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootGraph
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import io.github.kristoffer_consid.pokedex.R
import io.github.kristoffer_consid.pokedex.data.PreviewSpecies
import io.github.kristoffer_consid.pokedex.ui.theme.background


@OptIn(ExperimentalMaterial3Api::class)
@Destination<RootGraph>
@Composable
fun DetailsScreen(navigator: DestinationsNavigator, pokemonInfo: NamedApiResource) {
    val viewModel = hiltViewModel<DetailsViewModel, DetailsViewModel.DetailsViewModelFactory> { factory ->
        factory.create(pokemonInfo)
    }

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val isFavorite = remember(uiState.favorites) { uiState.favorites.contains(pokemonInfo.id) }

    Scaffold(
        topBar = {
            AppBar(
                title = "", //uiState.pokemonInfo.name.replaceFirstChar { it.uppercase() },
                onBackClick = { navigator.popBackStack() },
                actions = {
                    IconToggleButton(
                        checked = isFavorite,
                        onCheckedChange = { viewModel.toggleFavorite() }
                    ) {
                        if (isFavorite) {
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
                }
            )
        }
    ) { contentPadding ->
        if (uiState.isLoading) {
            Text("Loading")
        }
        else {
            DetailsDisplay(
                uiState,
                Modifier.padding(contentPadding)
            )
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
        modifier = modifier.background(MaterialTheme.colorScheme.primary),
        actions = actions,
        title = { Text(text = title) },
        scrollBehavior = scrollBehavior,
        windowInsets = WindowInsets(0.dp),
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary
        ),
        navigationIcon = {
            IconButton(onClick = onBackClick) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Localized description",
                    tint = MaterialTheme.colorScheme.onPrimary
                )
            }
        }
    )
}
@Composable
fun DetailsDisplay(uiState: DetailsUiState, modifier: Modifier = Modifier) {
    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = modifier.padding(
            start = 8.dp,
            end = 8.dp
        ).verticalScroll(rememberScrollState()),
    )  {
        Header(uiState.pokemonInfo)
        TitleTexts(uiState.pokemonInfo)
        InfoCard(uiState.species!!)
        EvolutionCard(uiState.evolutionChain)
    }
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
fun TitleTexts(pokemonInfo: NamedApiResource, modifier: Modifier = Modifier) {
    Column(modifier = modifier) {
        Text(
            "#${pokemonInfo.id}",
            style = MaterialTheme.typography.bodyMedium
        )

        Text(
            pokemonInfo.name.replaceFirstChar { it.uppercase() },
            style = MaterialTheme.typography.headlineLarge
        )
    }
}

@Composable
fun InfoCard(pokemonSpecies: PokemonSpecies, modifier: Modifier = Modifier) {
    val flavourText = pokemonSpecies.flavorTextEntries
        .filter { it.language.name == "en" }
        .random().flavorText.replace("\n", " ")

    // TODO: Replace with text carousel
    Card(modifier = modifier
        .fillMaxWidth()
        .wrapContentHeight(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondary
        )
    ) {
        Text(
            flavourText,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(12.dp),
            color = MaterialTheme.colorScheme.onSecondary
        )
    }
}

@Preview(showBackground = true)
@Composable
fun InfoCardPreview() {
    InfoCard(PreviewSpecies)
}

@Composable
fun EvolutionCard(evolutionChain: EvolutionChain?, modifier: Modifier = Modifier) {
    if (evolutionChain?.chain == null || evolutionChain.chain.evolvesTo.isEmpty()) {
        return
    }

    val firstPokemon = evolutionChain.chain.species

    Card(modifier = modifier
        .fillMaxWidth()
        .wrapContentHeight(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondary
        )
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.padding(
                top = 16.dp,
                bottom = 16.dp,
            )
        ) {
            EvolutionPart(
                firstPokemon,
                evolutionChain.chain.evolvesTo
            )
        }
    }
}

@Composable
fun EvolutionPart(
    previousPokemon: NamedApiResource,
    evolutionChain: List<ChainLink>
) {
    evolutionChain.forEach { evolution ->
        val evoDetails = evolution.evolutionDetails[0]
        val evoTrigger = when(evoDetails.trigger.name) {
            "level-up" -> {
                if (evoDetails.minLevel != null) {
                    "Level up to ${evoDetails.minLevel}"
                }

                "Level up"
            }
            "use-item" -> {
                if (evoDetails.item != null) {
                    "Use ${evoDetails.item!!.name.replace("-", " ").replaceFirstChar { it.uppercase() }}"
                }
                else {
                    "Special condition"
                }
            }
            "trade" -> "Trade"
            else -> "Special condition"
        }

        Column {
            Text(
                evoTrigger,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(
                    start = 16.dp
                ),
                color = MaterialTheme.colorScheme.onSecondary
            )

            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer
                ),
                modifier = Modifier
                    .padding(
                        start = 16.dp,
                        end = 16.dp
                    )
                    .fillMaxWidth(),
            ) {
                Row(
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .wrapContentHeight()
                        .fillMaxWidth()
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        AsyncImage(
                            model = ImageRequest.Builder(LocalContext.current)
                                .data("https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/${previousPokemon.id}.png")
                                .crossfade(true)
                                .build(),
                            placeholder = painterResource(R.drawable.pokeball),
                            contentDescription = previousPokemon.name,
                            modifier = Modifier.height(80.dp)
                        )

                        Text(
                            previousPokemon.name.replaceFirstChar { it.uppercase() },
                            style = MaterialTheme.typography.titleMedium,
                        )
                    }

                    Icon(
                        Icons.Default.ArrowForward,
                        contentDescription = "Evolves to",
                        tint = MaterialTheme.colorScheme.secondary
                    )

                    val nextPokemon = evolution.species
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        AsyncImage(
                            model = ImageRequest.Builder(LocalContext.current)
                                .data("https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/${nextPokemon.id}.png")
                                .crossfade(true)
                                .build(),
                            placeholder = painterResource(R.drawable.pokeball),
                            contentDescription = nextPokemon.name,
                            modifier = Modifier.height(80.dp)
                        )

                        Text(
                            nextPokemon.name.replaceFirstChar { it.uppercase() },
                            style = MaterialTheme.typography.titleMedium,
                        )
                    }
                }
            }
        }

        if (evolution.evolvesTo.isNotEmpty()) {
            EvolutionPart(
                evolution.species,
                evolution.evolvesTo
            )
        }
    }

//    Column(
//        horizontalAlignment = Alignment.CenterHorizontally,
//        modifier = modifier
//    ) {
//        AsyncImage(
//            model = ImageRequest.Builder(LocalContext.current)
//                .data("https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/${pokemon.id}.png")
//                .crossfade(true)
//                .build(),
//            placeholder = painterResource(R.drawable.pokeball),
//            contentDescription = pokemon.name
//        )
//
//        Text(
//            pokemon.name.replaceFirstChar { it.uppercase() },
//            style = MaterialTheme.typography.titleMedium,
//        )
//    }
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

