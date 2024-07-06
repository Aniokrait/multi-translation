package io.github.aniokrait.multitranslation.ui.navigation

import android.content.Context
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import io.github.aniokrait.multitranslation.extension.dataStore
import io.github.aniokrait.multitranslation.ui.screen.modeldownload.TranslationModelDownload
import io.github.aniokrait.multitranslation.ui.screen.modeldownload.TranslationModelDownloadScreen
import io.github.aniokrait.multitranslation.ui.screen.translate.Translation
import io.github.aniokrait.multitranslation.ui.screen.translate.TranslationScreen
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable

@Composable
fun MTransNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    snackBarMessage: MutableState<String>,
) {
    val startDestinationState: MutableState<StartDestination> = remember {
        mutableStateOf(Loading)
    }
    startDestination(
        context = LocalContext.current,
        scope = rememberCoroutineScope(),
        startDestinationState = startDestinationState,
    )

    NavHost(
        modifier = modifier.padding(horizontal = 8.dp),
        navController = navController,
        startDestination = startDestinationState.value
    ) {
        composable<Loading> {

        }
        composable<TranslationModelDownload> {
            TranslationModelDownloadScreen(
                navigateToTranslation = { navController.navigate(Translation) },
                snackBarMessage = snackBarMessage,
            )
        }
        composable<Translation> {
            TranslationScreen()
        }
    }

}

// If first launch, start destination is TranslationModelDownload, else Transaction.
private fun startDestination(
    context: Context,
    scope: CoroutineScope,
    startDestinationState: MutableState<StartDestination>,
) {
    scope.launch {
        val isFirstLaunch = context.dataStore.data.map {
            it.asMap().isEmpty()
        }.first()

        val startDestination = if (isFirstLaunch) {
            TranslationModelDownload
        } else {
            Translation
        }

        startDestinationState.value = startDestination
    }
}

interface StartDestination

@Serializable
private object Loading : StartDestination
