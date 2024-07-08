package io.github.aniokrait.multitranslation.ui.screen.modeldownload

import android.content.Context
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import io.github.aniokrait.multitranslation.R
import io.github.aniokrait.multitranslation.core.LanguageNameResolver
import io.github.aniokrait.multitranslation.core.NetworkChecker
import io.github.aniokrait.multitranslation.extension.ui.conditional
import io.github.aniokrait.multitranslation.ui.TopBar
import io.github.aniokrait.multitranslation.ui.navigation.StartDestination
import io.github.aniokrait.multitranslation.viewmodel.TranslationModelDownloadViewModel
import io.github.aniokrait.multitranslation.viewmodel.state.TranslationModelDownloadViewModelState
import kotlinx.serialization.Serializable
import org.koin.androidx.compose.koinViewModel
import java.util.Locale

@Serializable
object TranslationModelDownload : StartDestination

@Composable
fun TranslationModelDownloadScreen(
    modifier: Modifier = Modifier,
    vm: TranslationModelDownloadViewModel = koinViewModel(),
    snackBarMessage: MutableState<String>,
    navigateToTranslation: () -> Unit,
    onBackClicked: (() -> Unit)?,
) {
    val state = vm.downloadState.collectAsStateWithLifecycle().value
    TranslationModelDownloadScreen(
        modifier = modifier,
        state = state.languagesState,
        isDownloading = state.isDownloading,
        snackBarMessage = snackBarMessage,
        onCheckClicked = vm::onCheckClicked,
        onDownloadClicked = vm::onDownloadClicked,
        navigateToTranslation = navigateToTranslation,
        onBackClicked = onBackClicked,
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
private fun TranslationModelDownloadScreen(
    modifier: Modifier = Modifier,
    state: List<TranslationModelDownloadViewModelState.EachLanguageState>,
    isDownloading: Boolean,
    snackBarMessage: MutableState<String>,
    onCheckClicked: (Locale) -> Unit,
    onDownloadClicked: (Context, () -> Unit, String, MutableState<String>) -> Unit,
    navigateToTranslation: () -> Unit,
    onBackClicked: (() -> Unit)?,
) {
    Scaffold(
        topBar = {
            TopBar(
                onBackClicked = onBackClicked,
            )
        },
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
                        val eachLocaleState = state.find { it.locale == locale }
                        val checked: Boolean = eachLocaleState?.checked?.value ?: false
                        val downloaded: Boolean = eachLocaleState?.downloaded?.value ?: false

                        LanguageSelection(
                            locale = locale,
                            checked = checked,
                            downloaded = downloaded,
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


}

@Composable
private fun LanguageSelection(
    locale: Locale,
    checked: Boolean,
    downloaded: Boolean,
    onCheckedChange: (Boolean) -> Unit,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Interact checkbox ripple effect when text is clicked.
        val interactionSource = remember { MutableInteractionSource() }

        if (downloaded) {
            Icon(
                modifier = Modifier
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
                interactionSource = interactionSource
            )
        }

        Text(
            modifier = Modifier
                .conditional(condition = !downloaded) {
                    clickable(
                        interactionSource = interactionSource,
                        indication = null
                    ) {
                        onCheckedChange.invoke(checked)
                    }
                },
            text = locale.getDisplayLanguage(Locale.JAPANESE),
            style = MaterialTheme.typography.titleMedium,
        ) // TODO: Apply current system locale
    }
}

@Preview(showBackground = true)
@Composable
private fun TranslationModelDownloadScreenPreview() {
    val iceLandState = TranslationModelDownloadViewModelState.EachLanguageState(
        locale = Locale.forLanguageTag("is"),
        checked = remember { mutableStateOf(true) },
        downloaded = remember { mutableStateOf(false) },
    )
    val arabicState = TranslationModelDownloadViewModelState.EachLanguageState(
        locale = Locale.forLanguageTag("ar"),
        checked = remember { mutableStateOf(false) },
        downloaded = remember { mutableStateOf(true) },
    )

    TranslationModelDownloadScreen(
        state = listOf(iceLandState, arabicState),
        isDownloading = false,
        snackBarMessage = remember { mutableStateOf("") },
        onCheckClicked = {},
        onDownloadClicked = { _, _, _, _ -> },
        navigateToTranslation = {},
        onBackClicked = {},
    )
}
