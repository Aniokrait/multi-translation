package io.github.aniokrait.multitranslation.ui.screen.translate

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
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
    Button(
        modifier = modifier,
        enabled = !isTranslating,
        onClick = {
            keyboardController?.hide()
            onTranslateClick(translateSource)
        }
    ) {
        var textButtonSize by remember {
            mutableStateOf(IntSize(0, 0))
        }
        if (isTranslating) {
            val density = LocalDensity.current
            with(density) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .then(Modifier.size(32.dp))  // change indicator size.
                        .height(textButtonSize.height.toDp()) // fix button size.
                        .width(textButtonSize.width.toDp()), // fix button size.
                    strokeWidth = 4.dp,
                    color = MaterialTheme.colorScheme.secondary,
                    trackColor = MaterialTheme.colorScheme.surfaceVariant,
                )
            }
        } else {
            Text(
                modifier = Modifier.onGloballyPositioned {
                    textButtonSize = it.size
                },
                text = stringResource(id = R.string.btn_translation_button)
            )
            Spacer(modifier = Modifier.height(4.dp))
        }
    }
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
