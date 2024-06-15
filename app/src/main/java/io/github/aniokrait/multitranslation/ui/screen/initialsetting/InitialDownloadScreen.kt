package io.github.aniokrait.multitranslation.ui.screen.initialsetting

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.github.aniokrait.multitranslation.R
import io.github.aniokrait.multitranslation.core.LanguageNameResolver
import io.github.aniokrait.multitranslation.ui.stateholder.LanguageDownloadState
import io.github.aniokrait.multitranslation.viewmodel.InitialDownloadViewModel
import java.util.Locale

@Composable
fun InitialDownloadScreen(
    vm: InitialDownloadViewModel
) {
    val state = vm.downloadState.collectAsState()
    InitialDownloadScreen(state = state.value)
}


@Composable
private fun InitialDownloadScreen(
    modifier: Modifier = Modifier,
    state: List<LanguageDownloadState.EachLanguageState>,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(12.dp),
    ) {
        Text(
            text = stringResource(id = R.string.lbl_description_for_download_models),
            style = MaterialTheme.typography.headlineSmall,
            )
        Spacer(modifier = Modifier.height(12.dp))

        val allLocales = LanguageNameResolver.getAllLanguagesLabel()
        val oddLocales = allLocales.filterIndexed { index, _ -> index % 2 != 0 }
        val evenLocales = allLocales.filterIndexed { index, _ -> index % 2 == 0 }

        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column {
                for (locale in oddLocales) {
                    val a = state.find { it.locale == locale }
                    LanguageSelection(locale = locale, checked = a?.checked?.value ?: false, onCheckedChange = {})
                }
            }
            Column {
                for (locale in evenLocales) {
                    val a = state.find { it.locale == locale }
                    LanguageSelection(locale = locale, checked = a?.checked?.value ?: false, onCheckedChange = {})
                }
            }
        }
    }
}

@Composable
private fun LanguageSelection(
    locale: Locale,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
) {
    Row {
        Checkbox(checked = checked, onCheckedChange = onCheckedChange)
        Text(
            text = locale.getDisplayLanguage(Locale.JAPANESE),
            style = MaterialTheme.typography.titleMedium,
            ) // TODO: Apply current system locale
    }
}

@Preview(showBackground = true)
@Composable
private fun InitialDownloadScreenPreview() {
    InitialDownloadScreen(
        state = listOf()
    )
}
