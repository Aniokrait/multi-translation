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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.github.aniokrait.multitranslation.R
import io.github.aniokrait.multitranslation.core.LanguageNameResolver
import io.github.aniokrait.multitranslation.viewmodel.InitialDownloadViewModel

@Composable
fun InitialDownloadScreen(
    vm: InitialDownloadViewModel
) {
    InitialDownloadScreen()
}


@Composable
private fun InitialDownloadScreen(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(12.dp),
    ) {
        Text(text = stringResource(id = R.string.lbl_description_for_download_models))
        Spacer(modifier = Modifier.height(12.dp))


        val allLanguages = LanguageNameResolver.getAllLanguagesLabel()
        val oddLanguages = allLanguages.filterIndexed { index, _ -> index % 2 != 0 }
        val evenLanguages = allLanguages.filterIndexed { index, _ -> index % 2 == 0 }

        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column {
                for (language in oddLanguages) {
                    LanguageSelection(language = language,)
                }
            }
            Column {
                for (language in evenLanguages) {
                    LanguageSelection(language = language,)
                }
            }
        }
    }
}

@Composable
private fun LanguageSelection(
    language: String
) {
    Row {
//        Checkbox(checked = , onCheckedChange = )
        Text(text = language)
    }
}

@Preview(showBackground = true)
@Composable
private fun InitialDownloadScreenPreview() {
    InitialDownloadScreen()
}
