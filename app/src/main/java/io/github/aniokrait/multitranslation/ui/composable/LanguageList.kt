package io.github.aniokrait.multitranslation.ui.composable

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.github.aniokrait.multitranslation.R
import io.github.aniokrait.multitranslation.extension.ui.conditional
import io.github.aniokrait.multitranslation.ui.stateholder.EachLanguageState
import java.util.Locale

/**
 * List of languages.
 * When already downloaded, show download done icon, otherwise show checkbox.
 *
 * @param modifier Layout modifier.
 * @param locales List of languages to show.
 * @param state States of each language if it is downloaded, checked or not.
 * @param onCheckClicked Lambda to update the language checked state.
 */
@Composable
fun LanguageList(
    modifier: Modifier = Modifier,
    locales: List<Locale>,
    state: List<EachLanguageState>,
    onCheckClicked: (Locale) -> Unit,
) {
    LazyVerticalGrid(
        modifier = modifier,
        columns = GridCells.Fixed(2),
    ) {
        items(locales.filter { it.language != Locale.getDefault().language }) { locale ->
            val eachLocaleState = state.find { it.locale == locale }
            val checked: Boolean = eachLocaleState?.checked?.value ?: false
            val downloaded: Boolean = eachLocaleState?.downloaded?.value ?: false

            LanguageSelection(
                locale = locale,
                checked = checked,
                downloaded = downloaded,
                onCheckedChange = { onCheckClicked(locale) },
            )
        }
    }
}

@Composable
private fun LanguageSelection(
    locale: Locale,
    checked: Boolean,
    downloaded: Boolean,
    onCheckedChange: (Boolean) -> Unit,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
    ) {
        // Interact checkbox ripple effect when text is clicked.
        val interactionSource = remember { MutableInteractionSource() }

        if (downloaded) {
            Icon(
                modifier =
                Modifier
                    .size(48.dp)
                    .padding(12.dp),
                painter = painterResource(id = R.drawable.download_done_24px),
                contentDescription = null,
                tint = Color.Gray,
            )
        } else {
            Checkbox(
                checked = checked,
                onCheckedChange = onCheckedChange,
                interactionSource = interactionSource,
            )
        }

        Text(
            modifier =
            Modifier
                .conditional(condition = !downloaded) {
                    clickable(
                        interactionSource = interactionSource,
                        indication = null,
                    ) {
                        onCheckedChange.invoke(checked)
                    }
                },
            text = locale.getDisplayLanguage(Locale.getDefault()),
            style = MaterialTheme.typography.titleMedium,
        ) // TODO: Apply current system locale
    }
}

@Preview(showBackground = true)
@Composable
fun LanguageListPreview() {
    LanguageList(
        locales =
        listOf(
            Locale.JAPANESE,
            Locale.CHINESE,
            Locale.GERMAN,
            Locale.ENGLISH,
        ),
        state =
        listOf(
            EachLanguageState(
                locale = Locale.JAPANESE,
                checked = remember { mutableStateOf(true) },
                downloaded = remember { mutableStateOf(false) },
            ),
            EachLanguageState(
                locale = Locale.CHINESE,
                checked = remember { mutableStateOf(false) },
                downloaded = remember { mutableStateOf(true) },
            ),
            EachLanguageState(
                locale = Locale.GERMAN,
                checked = remember { mutableStateOf(true) },
                downloaded = remember { mutableStateOf(true) },
            ),
            EachLanguageState(
                locale = Locale.ENGLISH,
                checked = remember { mutableStateOf(false) },
                downloaded = remember { mutableStateOf(false) },
            ),
        ),
        onCheckClicked = {},
    )
}
