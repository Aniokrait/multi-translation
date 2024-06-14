package io.github.aniokrait.multitranslation.ui.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import io.github.aniokrait.multitranslation.R
import io.github.aniokrait.multitranslation.viewmodel.TranslationViewModel

@Composable
private fun TranslationScreen(
    modifier: Modifier = Modifier,
    vm: TranslationViewModel = TranslationViewModel(),
) {
    TranslationScreen(
        modifier = modifier,
        translateResults = vm.translationResult,
        onTranslateClick = vm::onTranslateClick,
    )
}

@Composable
fun TranslationScreen(
    modifier: Modifier = Modifier,
    translateResults: Map<String, String>,
    textBlockHeight: Dp = 100.dp,
    onTranslateClick: (String) -> Unit,
) {
    Column(
        modifier = modifier
            .statusBarsPadding()
            .fillMaxSize()
    ) {

        TranslateSourceArea(onTranslateClick = onTranslateClick,)

        ResultArea(textBlockHeight = textBlockHeight, translateResults = translateResults)
    }
}

// Translation source area.
@OptIn(ExperimentalComposeUiApi::class)
@Composable
private fun ColumnScope.TranslateSourceArea(
    onTranslateClick: (String) -> Unit,
) {
    val input = remember { mutableStateOf("") }
    // TODO: Fix height and show scrollbar
    TextField(
        modifier = Modifier.fillMaxWidth(),
        label = { Text(text = stringResource(id = R.string.lbl_translation_source))},
        value = input.value,
        onValueChange = { input.value = it },
    )

    val keyboardController = LocalSoftwareKeyboardController.current
    Button(
        modifier = Modifier
            .width(140.dp)
            .padding(top = 16.dp)
            .align(Alignment.CenterHorizontally)
        ,
        onClick = {
            keyboardController?.hide()
            onTranslateClick(input.value)
        }
    ) {
        Text(text = stringResource(id = R.string.btn_translation_button))
    }
}

// Translation results.
@Composable
private fun ResultArea(
    textBlockHeight: Dp,
    translateResults: Map<String, String>,
) {
    Text(
        text = stringResource(id = R.string.lbl_translation_result),
        modifier = Modifier.padding(bottom = 4.dp)
    )

    for ((language, result) in translateResults) {
        TranslateResultCard(
            modifier = Modifier.padding(bottom = 8.dp),
            textBlockHeight = textBlockHeight,
            language = language,
            content = result,
        )
    }
}

@Preview
@Composable
fun TranslationScreenPreview() {
    TranslationScreen(
        translateResults = mapOf("ja" to "こんにちは", "en" to "Hello"),
        onTranslateClick = {},
    )
}
