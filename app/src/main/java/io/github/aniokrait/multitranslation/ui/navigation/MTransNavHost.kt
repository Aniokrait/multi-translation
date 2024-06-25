package io.github.aniokrait.multitranslation.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
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
        modifier = modifier,
        navController = navController,
        startDestination = InitialDownload
    ) {
        composable<InitialDownload> {
            InitialDownloadScreen()
        }
        composable<Translation> {
            TranslationScreen()
        }
    }
}

