package io.github.aniokrait.multitranslation.ui.screen.initialsetting

import android.content.Context
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.github.aniokrait.multitranslation.R
import io.github.aniokrait.multitranslation.core.LanguageNameResolver
import io.github.aniokrait.multitranslation.ui.stateholder.InitialDownloadScreenState
import io.github.aniokrait.multitranslation.viewmodel.InitialDownloadViewModel
import kotlinx.serialization.Serializable
import org.koin.androidx.compose.koinViewModel
import java.util.Locale

@Serializable
object InitialDownload

@Composable
fun InitialDownloadScreen(
    modifier: Modifier = Modifier,
    vm: InitialDownloadViewModel = koinViewModel(),
) {
    val state = vm.downloadState.collectAsState()
    InitialDownloadScreen(
        modifier = modifier,
        state = state.value,
        vm::onCheckClicked,
        vm::onDownloadClicked,
    )
}


@Composable
private fun InitialDownloadScreen(
    modifier: Modifier = Modifier,
    state: List<InitialDownloadScreenState.EachLanguageState>,
    onCheckClicked: (Locale) -> Unit,
    onDownloadClicked: (Context) -> Unit,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
    ) {
        Text(
            text = stringResource(id = R.string.lbl_description_for_download_models),
            style = MaterialTheme.typography.headlineSmall,
        )
        Text(
            text = stringResource(id = R.string.lbl_hosoku),
            style = MaterialTheme.typography.bodyMedium
        )
        Spacer(modifier = Modifier.height(12.dp))

        val allLocales = LanguageNameResolver.getAllLanguagesLabel()
        LazyVerticalGrid(
            modifier = Modifier.weight(1f),
            columns = GridCells.Fixed(2)
        ) {
            items(allLocales) { locale ->
                val checked: Boolean = state.find { it.locale == locale }?.checked?.value ?: false
                LanguageSelection(
                    locale = locale,
                    checked = checked,
                    onCheckedChange = { onCheckClicked(locale) }
                )
            }
        }

        val context = LocalContext.current
        Button(
            modifier = Modifier.align(Alignment.CenterHorizontally),
            onClick = { onDownloadClicked(context) },
        ) {
            Text(text = stringResource(id = R.string.btn_download_translation_model))
        }
    }

}

@Composable
private fun LanguageSelection(
    locale: Locale,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
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
        state = listOf(),
        onCheckClicked = {},
        onDownloadClicked = {},
    )
}
