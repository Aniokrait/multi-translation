package io.github.aniokrait.multitranslation.ui.screen.translate

import SuspendableButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import io.github.aniokrait.multitranslation.R

/**
 * The button to translate text.
 *
 * @param modifier Modifier
 * @param isTranslating Whether the button is translating.
 * @param translateSource The source text to translate.
 * @param onTranslateClick Called when the button is clicked.
 */
@Composable
fun TranslateButton(
    modifier: Modifier = Modifier,
    isTranslating: Boolean,
    translateSource: String,
    onTranslateClick: (String) -> Unit,
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    SuspendableButton(
        modifier = modifier,
        isSuspending = isTranslating,
        text = stringResource(id = R.string.feature_translation_translate_by_google),
        onClick = {
            keyboardController?.hide()
            onTranslateClick(translateSource)
        },
    )
}

@Preview
@Composable
private fun TranslateButtonDefaultPreview() {
    TranslateButton(
        isTranslating = false,
        translateSource = "",
        onTranslateClick = {},
    )
}

@Preview
@Composable
private fun TranslateButtonTranslatingPreview() {
    TranslateButton(
        isTranslating = true,
        translateSource = "",
        onTranslateClick = {},
    )
}
