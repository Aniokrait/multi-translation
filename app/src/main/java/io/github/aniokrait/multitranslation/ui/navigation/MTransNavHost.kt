package io.github.aniokrait.multitranslation.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import io.github.aniokrait.multitranslation.ui.screen.initialsetting.InitialDownload
import io.github.aniokrait.multitranslation.ui.screen.initialsetting.InitialDownloadScreen
import io.github.aniokrait.multitranslation.ui.screen.translate.Translation
import io.github.aniokrait.multitranslation.ui.screen.translate.TranslationScreen

@Composable
fun MTransNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController,
) {
    NavHost(
        modifier = modifier.padding(horizontal = 8.dp),
        navController = navController,
        startDestination = InitialDownload
    ) {
        composable<InitialDownload> {
            InitialDownloadScreen(navigateToTranslation = { navController.navigate(Translation) })
        }
        composable<Translation> {
            TranslationScreen()
        }
    }
}

