package io.github.aniokrait.multitranslation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import io.github.aniokrait.multitranslation.ui.TopBar
import io.github.aniokrait.multitranslation.ui.screen.TranslationScreen
import io.github.aniokrait.multitranslation.ui.theme.MultiTranslationTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Box(Modifier.safeDrawingPadding()) {
                MultiTranslationTheme {
                    Scaffold(
                        modifier = Modifier.fillMaxSize(),
                        topBar = { TopBar() },
                    ) { innerPadding ->
                        TranslationScreen(
                            modifier = Modifier.padding(innerPadding),
                            translateResults = mapOf(),
                            onTranslateClick = {},
                        )
                    }
                }
            }
        }
    }
}
