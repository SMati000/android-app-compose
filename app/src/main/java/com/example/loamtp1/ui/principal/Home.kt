package com.example.loamtp1.ui.principal

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

private val DarkColorPalette = darkColorScheme(
    primary = Color(0xFFBB86FC),       // Purple200
    secondary = Color(0xFF03DAC5)      // Teal200
)

@Composable
fun AppTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = DarkColorPalette,
        content = content
    )
}

/*
* Composable functions are stateless and declarative
* A Composable is supposed to just describe UI
* Compose decides when and how often to re-run them (this is called recomposition).
* Compose will re-run (recompose) your Composables whenever state changes
* Compose builds and updates the UI for you.
* Should not be called like a normal function in business logic — it’s only for UI.
*/
@Composable
fun HomeScreen() {
    Box(
        Modifier
            .fillMaxSize()
            .padding(WindowInsets.systemBars.asPaddingValues())
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            AppHeader(Modifier.weight(2f))
            PriceRefs(Modifier.weight(3f))
            RowEspacio(Modifier.weight(5f))
            RowGrabar(Modifier.weight(5f))
            RowOtros(Modifier.weight(5f))
            AppFooter(Modifier.weight(2f))
        }
    }
}
