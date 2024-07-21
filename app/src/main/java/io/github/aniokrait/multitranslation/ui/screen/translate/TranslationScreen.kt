package io.github.aniokrait.multitranslation.ui.screen.translate

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import io.github.aniokrait.multitranslation.R
import io.github.aniokrait.multitranslation.ui.TopBar
import io.github.aniokrait.multitranslation.ui.navigation.StartDestination
import io.github.aniokrait.multitranslation.viewmodel.TranslationViewModel
import kotlinx.serialization.Serializable
import org.koin.androidx.compose.koinViewModel
import java.util.Locale

@Serializable
object Translation : StartDestination

@Composable
fun TranslationScreen(
    modifier: Modifier = Modifier,
    vm: TranslationViewModel = koinViewModel(),
    onAddModelClicked: () -> Unit,
    onDeleteModelClicked: () -> Unit,
) {
    val uiState = vm.uiState.collectAsStateWithLifecycle().value
    TranslationScreen(
        modifier = modifier,
        translateResults = uiState.translationResult,
        isTranslating = uiState.isTranslating,
        onAddModelClicked = onAddModelClicked,
        onDeleteModelClicked = onDeleteModelClicked,
        onTranslateClick = vm::onTranslateClick,
    )
}

@Composable
private fun TranslationScreen(
    modifier: Modifier = Modifier,
    translateResults: Map<Locale, String>,
    isTranslating: Boolean,
    textBlockHeight: Dp = 100.dp,
    onAddModelClicked: () -> Unit,
    onDeleteModelClicked: () -> Unit,
    onTranslateClick: (String) -> Unit,
) {
    Scaffold(
        topBar = {
            TopBar(
                showTrailingIcon = true,
                onDeleteModelClicked = onDeleteModelClicked,
                onAddModelClicked = onAddModelClicked,
            )
        }
    ) { innerPadding ->
        Box {
            var bannerHeight by remember {
                mutableIntStateOf(0)
            }

            Column(
                modifier = modifier
                    .statusBarsPadding()
                    .padding(innerPadding)
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
            ) {

                TranslateSourceArea(
                    isTranslating = isTranslating,
                    onTranslateClick = onTranslateClick
                )

                ResultArea(
                    textBlockHeight = textBlockHeight,
                    translateResults = translateResults,
                )

                val density = LocalDensity.current
                with(density) {
                    // add height so that the bottom card comes above the banner.
                    Spacer(modifier = Modifier.height(bannerHeight.toDp()))
                }
            }

            BannerAd(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .onGloballyPositioned {
                        bannerHeight = it.size.height
                    }
            )
        }
    }
}

// Translation source area.
@Composable
private fun ColumnScope.TranslateSourceArea(
    isTranslating: Boolean,
    onTranslateClick: (String) -> Unit,
) {
    val input = remember { mutableStateOf("") }
    // TODO: Fix height and show scrollbar
    TextField(
        modifier = Modifier.fillMaxWidth(),
        label = { Text(text = stringResource(id = R.string.lbl_translation_source)) },
        value = input.value,
        onValueChange = { input.value = it },
    )

    TranslateButton(
        modifier = Modifier
            .width(140.dp)
            .padding(top = 16.dp)
            .align(Alignment.CenterHorizontally),
        isTranslating = isTranslating,
        translateSource = input.value,
        onTranslateClick = onTranslateClick,
    )
}

// Translation results.
@Composable
private fun ResultArea(
    textBlockHeight: Dp,
    translateResults: Map<Locale, String>,
) {
    Text(
        text = stringResource(id = R.string.lbl_translation_result),
        style = MaterialTheme.typography.titleMedium,
        modifier = Modifier.padding(bottom = 4.dp)
    )

    Spacer(modifier = Modifier.height(4.dp))

    for ((language, result) in translateResults) {
        // FIXME: Hide Japanese till implementing changing source language.
        if (language == Locale.JAPANESE) {
            continue
        }

        TranslateResultCard(
            modifier = Modifier.padding(bottom = 8.dp),
            textBlockHeight = textBlockHeight,
            language = language.getDisplayName(Locale.getDefault()),
            content = result,
        )
    }
}

@Preview(showBackground = true)
@Composable
fun TranslationScreenPreview() {
    TranslationScreen(
        translateResults = mapOf(Locale.JAPANESE to "こんにちは", Locale.ENGLISH to "Hello"),
        isTranslating = false,
        onTranslateClick = { _ -> },
        onAddModelClicked = {},
        onDeleteModelClicked = {},
    )
}
