package io.github.aniokrait.multitranslation.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import io.github.aniokrait.multitranslation.ui.navigation.MTransNavHost
import io.github.aniokrait.multitranslation.ui.screen.initialsetting.InitialDownload
import io.github.aniokrait.multitranslation.ui.theme.MultiTranslationTheme

@Composable
fun MTransApp() {
    val navController = rememberNavController()

    Box(Modifier.safeDrawingPadding()) {
        MultiTranslationTheme {
            Scaffold(
                modifier = Modifier.fillMaxSize(),
                topBar = { TopBar(
                    onSettingsClick = { navController.navigate(InitialDownload) }
                ) },
            ) { innerPadding ->
                MTransNavHost(
                    modifier = Modifier.padding(innerPadding),
                    navController = navController,
                )
            }
        }
    }
}
