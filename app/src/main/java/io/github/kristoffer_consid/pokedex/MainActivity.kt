package io.github.kristoffer_consid.pokedex

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import dagger.hilt.android.AndroidEntryPoint
import io.github.kristoffer_consid.pokedex.ui.PokedexApp
import io.github.kristoffer_consid.pokedex.ui.theme.primary

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PokedexApp()

            SideEffect {
                WindowCompat.setDecorFitsSystemWindows(window, false)

                window.statusBarColor = primary.toArgb()
                window.navigationBarColor = primary.toArgb()

                ViewCompat.getWindowInsetsController(window.decorView).apply {
                    this?.isAppearanceLightStatusBars = false
                    this?.isAppearanceLightNavigationBars = false
                }
            }
        }
    }
}