package io.github.aniokrait.multitranslation.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import io.github.aniokrait.multitranslation.ui.navigation.MTransNavHost
import io.github.aniokrait.multitranslation.ui.screen.modeldownload.TranslationModelDownload
import io.github.aniokrait.multitranslation.ui.theme.MultiTranslationTheme

@Composable
fun MTransApp() {
    val navController = rememberNavController()

    Box(Modifier.safeDrawingPadding()) {
        MultiTranslationTheme {
            val snackBarHostState = remember { SnackbarHostState() }
            val snackBarMessage = remember { mutableStateOf("") }
            Scaffold(
                modifier = Modifier.fillMaxSize(),
                topBar = {
                    TopBar(
                        onSettingsClick = { navController.navigate(TranslationModelDownload) }
                    )
                },
                snackbarHost = { SnackbarHost(hostState = snackBarHostState) }
            ) { innerPadding ->
                MTransNavHost(
                    modifier = Modifier.padding(innerPadding),
                    navController = navController,
                    snackBarMessage = snackBarMessage,
                )

                LaunchedEffect(snackBarMessage.value) {
                    if (snackBarMessage.value != "") {
                        snackBarHostState.showSnackbar(snackBarMessage.value)
                        snackBarMessage.value = ""
                    }
                }

            }
        }
    }
}
