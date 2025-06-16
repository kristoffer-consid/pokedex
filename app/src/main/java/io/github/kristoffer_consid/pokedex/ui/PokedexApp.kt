package io.github.kristoffer_consid.pokedex.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.ramcosta.composedestinations.DestinationsNavHost
import io.github.kristoffer_consid.pokedex.ui.theme.PokedexTheme
import com.ramcosta.composedestinations.generated.NavGraphs

@Composable
fun PokedexApp() {
    PokedexTheme {
        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
            DestinationsNavHost(
                navGraph = NavGraphs.root,
                modifier = Modifier.padding(innerPadding)
            )
        }
    }
}