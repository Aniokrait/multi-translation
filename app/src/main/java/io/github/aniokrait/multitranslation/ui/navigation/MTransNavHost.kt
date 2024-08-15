package io.github.aniokrait.multitranslation.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.splashscreen.SplashScreen
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import io.github.aniokrait.multitranslation.extension.dataStore
import io.github.aniokrait.multitranslation.ui.screen.inquiry.Inquiry
import io.github.aniokrait.multitranslation.ui.screen.inquiry.InquiryScreen
import io.github.aniokrait.multitranslation.ui.screen.modeldelete.DeleteModel
import io.github.aniokrait.multitranslation.ui.screen.modeldelete.DeleteModelScreen
import io.github.aniokrait.multitranslation.ui.screen.modeldownload.TranslationModelDownload
import io.github.aniokrait.multitranslation.ui.screen.modeldownload.TranslationModelDownloadScreen
import io.github.aniokrait.multitranslation.ui.screen.translate.Translation
import io.github.aniokrait.multitranslation.ui.screen.translate.TranslationScreen
import io.github.aniokrait.multitranslation.viewmodel.MainViewModel
import kotlinx.serialization.Serializable
import org.koin.androidx.compose.koinViewModel

@Composable
fun MTransNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    snackBarMessage: MutableState<String>,
    mainViewModel: MainViewModel = koinViewModel(),
    setKeepOnScreenCondition: (SplashScreen.KeepOnScreenCondition) -> Unit,
) {
    val startDestinationState: MutableState<StartDestination> =
        remember {
            mutableStateOf(Loading)
        }

    LaunchedEffect(Unit) {
        startDestination(
            startDestinationState = startDestinationState,
            mainViewModel = mainViewModel,
        )
    }

    NavHost(
        modifier = modifier.padding(horizontal = 8.dp),
        navController = navController,
        startDestination = startDestinationState.value,
    ) {
        composable<Loading> {
        }
        composable<TranslationModelDownload> {
            TranslationModelDownloadScreen(
                navigateToTranslation = { navController.navigate(Translation) },
                snackBarMessage = snackBarMessage,
                onBackClicked =
                if (navController.previousBackStackEntry != null) {
                    {
                        ->
                        navController.navigateUp()
                    }
                } else {
                    null
                },
            )

            val context = LocalContext.current
            LaunchedEffect(Unit) {
                context.dataStore.edit { settings ->
                    settings[booleanPreferencesKey(IS_FIRST_LAUNCH)] = true
                }
            }
        }
        composable<Translation> {
            TranslationScreen(
                onAddModelClicked = { navController.navigate(TranslationModelDownload) },
                onDeleteModelClicked = { navController.navigate(DeleteModel) },
                onInquiryClicked = { navController.navigate(Inquiry) },
            )
        }
        composable<DeleteModel> {
            DeleteModelScreen(
                onBackClicked = navController::navigateUp,
            )
        }
        composable<Inquiry> {
            InquiryScreen(
                onBackClicked = navController::navigateUp,
            )
        }
    }

    LaunchedEffect(startDestinationState.value) {
        if (startDestinationState.value != Loading) {
            setKeepOnScreenCondition.invoke { false }
        }
    }
}

// If first launch, start destination is TranslationModelDownload, else Transaction.
private suspend fun startDestination(
    startDestinationState: MutableState<StartDestination>,
    mainViewModel: MainViewModel,
) {
    val isFirstLaunch = mainViewModel.checkIfFirstLaunch()

    val startDestination =
        if (isFirstLaunch) {
            TranslationModelDownload
        } else {
            Translation
        }

    startDestinationState.value = startDestination
}

interface StartDestination

@Serializable
private object Loading : StartDestination

const val IS_FIRST_LAUNCH = "is_first_launch"
