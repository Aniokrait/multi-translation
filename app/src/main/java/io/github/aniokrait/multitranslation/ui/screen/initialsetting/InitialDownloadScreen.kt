package io.github.aniokrait.multitranslation.ui.screen.initialsetting

import android.content.Context
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import io.github.aniokrait.multitranslation.R
import io.github.aniokrait.multitranslation.core.LanguageNameResolver
import io.github.aniokrait.multitranslation.core.NetworkChecker
import io.github.aniokrait.multitranslation.ui.navigation.StartDestination
import io.github.aniokrait.multitranslation.viewmodel.InitialDownloadViewModel
import io.github.aniokrait.multitranslation.viewmodel.state.InitialDownloadViewModelState
import kotlinx.serialization.Serializable
import org.koin.androidx.compose.koinViewModel
import java.util.Locale

@Serializable
object InitialDownload : StartDestination

@Composable
fun InitialDownloadScreen(
    modifier: Modifier = Modifier,
    vm: InitialDownloadViewModel = koinViewModel(),
    snackBarMessage: MutableState<String>,
    navigateToTranslation: () -> Unit,
) {
    val state = vm.downloadState.collectAsStateWithLifecycle().value
    InitialDownloadScreen(
        modifier = modifier,
        state = state.languagesState,
        isDownloading = state.isDownloading,
        snackBarMessage = snackBarMessage,
        onCheckClicked = vm::onCheckClicked,
        onDownloadClicked = vm::onDownloadClicked,
        navigateToTranslation = navigateToTranslation,
    )
    if (state.allDownloadFailed) {
        AlertDialog(
            text = {
                Text(text = stringResource(id = R.string.lbl_all_download_failed))
            },
            onDismissRequest = vm::onDownloadFailedDialogOkClicked,
            confirmButton = {
                Button(onClick = vm::onDownloadFailedDialogOkClicked) {
                    Text(text = "OK")
                }
            }
        )
    }
}


@Composable
private fun InitialDownloadScreen(
    modifier: Modifier = Modifier,
    state: List<InitialDownloadViewModelState.EachLanguageState>,
    isDownloading: Boolean,
    snackBarMessage: MutableState<String>,
    onCheckClicked: (Locale) -> Unit,
    onDownloadClicked: (Context, () -> Unit, String, MutableState<String>) -> Unit,
    navigateToTranslation: () -> Unit,
) {
    Box(
        modifier = Modifier.fillMaxSize()
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
                    val checked: Boolean =
                        state.find { it.locale == locale }?.checked?.value ?: false
                    LanguageSelection(
                        locale = locale,
                        checked = checked,
                        onCheckedChange = { onCheckClicked(locale) }
                    )
                }
            }

            val context = LocalContext.current
            val errorMessageTemplate =
                stringResource(id = R.string.msg_download_failed_for_these_languages)
            val showConfirmDownloadDialog = remember {
                mutableStateOf(false)
            }
            Button(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                onClick = {
                    val isWifiConnected = NetworkChecker.isWifiConnected(context = context)
                    if (isWifiConnected) {
                        onDownloadClicked(
                            context,
                            navigateToTranslation,
                            errorMessageTemplate,
                            snackBarMessage
                        )
                    } else {
                        showConfirmDownloadDialog.value = true
                    }

                },
            ) {
                Text(text = stringResource(id = R.string.btn_download_translation_model))
            }

            if (showConfirmDownloadDialog.value) {
                DownloadConfirmDialog(
                    showConfirmDownloadDialog = showConfirmDownloadDialog,
                    context = context,
                    navigateToTranslation = navigateToTranslation,
                    onProceedClicked = onDownloadClicked,
                    errorMessageTemplate = errorMessageTemplate,
                    snackBarMessage = snackBarMessage,
                )
            }
        }

        if (isDownloading) {
            DownloadingDialog()
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
        isDownloading = false,
        snackBarMessage = remember { mutableStateOf("") },
        onCheckClicked = {},
        onDownloadClicked = { _, _, _, _ -> },
        navigateToTranslation = {},
    )
}
