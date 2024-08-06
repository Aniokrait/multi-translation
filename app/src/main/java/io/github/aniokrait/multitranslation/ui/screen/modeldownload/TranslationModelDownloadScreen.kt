package io.github.aniokrait.multitranslation.ui.screen.modeldownload

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import io.github.aniokrait.multitranslation.ui.TopBar
import io.github.aniokrait.multitranslation.ui.composable.ConfirmDialog
import io.github.aniokrait.multitranslation.ui.composable.LanguageList
import io.github.aniokrait.multitranslation.ui.navigation.StartDestination
import io.github.aniokrait.multitranslation.ui.stateholder.EachLanguageState
import io.github.aniokrait.multitranslation.viewmodel.TranslationModelDownloadViewModel
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
    val state = vm.uiState.collectAsStateWithLifecycle().value
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
                Text(text = stringResource(id = R.string.feature_download_models_download_failed_all))
            },
            onDismissRequest = vm::onDownloadFailedDialogOkClicked,
            confirmButton = {
                Button(onClick = vm::onDownloadFailedDialogOkClicked) {
                    Text(text = "OK")
                }
            },
        )
    }
}

@Composable
private fun TranslationModelDownloadScreen(
    modifier: Modifier = Modifier,
    state: List<EachLanguageState>,
    isDownloading: Boolean,
    snackBarMessage: MutableState<String>,
    onCheckClicked: (Locale) -> Unit,
    onDownloadClicked: (() -> Unit, String, MutableState<String>, Boolean) -> Unit,
    navigateToTranslation: () -> Unit,
    onBackClicked: (() -> Unit)?,
) {
    Scaffold(
        topBar = {
            TopBar(
                title = stringResource(id = R.string.topbar_lbl_add_model),
                showTrailingIcon = false,
                onAddModelClicked = {},
                onDeleteModelClicked = {},
                onInquiryClicked = {},
                onBackClicked = onBackClicked,
            )
        },
    ) { innerPadding ->
        Box(
            modifier =
            Modifier
                .fillMaxSize()
                .padding(innerPadding),
        ) {
            Column(
                modifier =
                modifier
                    .fillMaxSize(),
            ) {
                Text(
                    text = stringResource(id = R.string.feature_download_models_choose_language_desc),
                    style = MaterialTheme.typography.headlineSmall,
                )
                Text(
                    text = stringResource(id = R.string.feature_download_models_model_size_desc),
                    style = MaterialTheme.typography.bodyMedium,
                )
                Spacer(modifier = Modifier.height(12.dp))

                val allLocales = LanguageNameResolver.getAllLanguagesLabel()
                LanguageList(
                    modifier = Modifier.weight(1f),
                    locales = allLocales,
                    state = state,
                    onCheckClicked = onCheckClicked,
                )

                val context = LocalContext.current
                val errorMessageTemplate =
                    stringResource(id = R.string.feature_download_models_download_failed_partially)
                var showNoNetworkDialog by
                remember {
                    mutableStateOf(false)
                }
                val showNoWifiAlertDialog =
                    remember {
                        mutableStateOf(false)
                    }
                val showDownloadConfirmDialog =
                    remember {
                        mutableStateOf(false)
                    }

                Button(
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    onClick = {
                        val isNetworkConnected =
                            NetworkChecker.isNetworkConnected(context = context)

                        if (isNetworkConnected) {
                            val isWifiConnected = NetworkChecker.isWifiConnected(context = context)
                            if (isWifiConnected) {
                                showDownloadConfirmDialog.value = true
                            } else {
                                showNoWifiAlertDialog.value = true
                            }
                        } else {
                            showNoNetworkDialog = true
                        }
                    },
                ) {
                    Text(text = stringResource(id = R.string.feature_download_models_download))
                }

                val trafficVolume = (state.filter { it.checked.value }.size * 30).toString()
                if (showNoWifiAlertDialog.value) {
                    ConfirmDialog(
                        showConfirmDialog = showNoWifiAlertDialog,
                        dialogText =
                        stringResource(
                            id = R.string.feature_download_models_confirm_no_wifi,
                            trafficVolume,
                        ),
                        confirmButtonText = R.string.feature_download_models_proceed,
                        dismissButtonText = R.string.feature_download_models_use_wifi_later,
                        onConfirmClicked = {
                            onDownloadClicked(
                                navigateToTranslation,
                                errorMessageTemplate,
                                snackBarMessage,
                                true,
                            )
                        },
                    )
                } else if (showDownloadConfirmDialog.value) {
                    val checkedLanguages =
                        state
                            .filter { it.checked.value }
                            .fold("") { acc, each ->
                                acc + "・" + each.locale.displayLanguage + System.lineSeparator()
                            }
                    ConfirmDialog(
                        showConfirmDialog = showDownloadConfirmDialog,
                        dialogText =
                        stringResource(
                            id = R.string.feature_download_models_confirm_download,
                            checkedLanguages,
                            trafficVolume,
                        ),
                        confirmButtonText = R.string.feature_download_models_proceed,
                        dismissButtonText = R.string.feature_download_models_cancel,
                        onConfirmClicked = {
                            onDownloadClicked(
                                navigateToTranslation,
                                errorMessageTemplate,
                                snackBarMessage,
                                false,
                            )
                        },
                    )
                } else if (showNoNetworkDialog) {
                    AlertDialog(
                        onDismissRequest = {
                            showNoNetworkDialog = false
                        },
                        confirmButton = {
                            Button(onClick = { showNoNetworkDialog = false }) {
                                Text(text = "OK")
                            }
                        },
                        text = {
                            Text(text = stringResource(id = R.string.feature_download_models_no_network))
                        }
                    )
                }
            }

            if (isDownloading) {
                DownloadingDialog()
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun TranslationModelDownloadScreenPreview() {
    val iceLandState =
        EachLanguageState(
            locale = Locale.forLanguageTag("is"),
            checked = remember { mutableStateOf(true) },
            downloaded = remember { mutableStateOf(false) },
        )
    val arabicState =
        EachLanguageState(
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
