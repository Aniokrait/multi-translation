package io.github.aniokrait.multitranslation.ui.screen.modeldelete

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import io.github.aniokrait.multitranslation.R
import io.github.aniokrait.multitranslation.ui.TopBar
import io.github.aniokrait.multitranslation.ui.composable.ConfirmDialog
import io.github.aniokrait.multitranslation.ui.composable.DangerActionButton
import io.github.aniokrait.multitranslation.ui.composable.LanguageList
import io.github.aniokrait.multitranslation.ui.stateholder.EachLanguageState
import io.github.aniokrait.multitranslation.viewmodel.DeleteModelViewModel
import kotlinx.serialization.Serializable
import org.koin.androidx.compose.koinViewModel
import java.util.Locale

@Serializable
object DeleteModel

/**
 * The screen for deleting translation models.
 * @param modifier Modifier.
 * @param vm ViewModel.
 * @param snackBarMessage Message for SnackBar.
 * @param navigateToTranslation After delete, navigate to Translation screen.
 * @param onBackClicked Back to the previous screen.
 */
@Composable
fun DeleteModelScreen(
    modifier: Modifier = Modifier,
    vm: DeleteModelViewModel = koinViewModel(),
    snackBarMessage: MutableState<String>,
    navigateToTranslation: () -> Unit,
    onBackClicked: (() -> Unit)?,
) {
    val state = vm.uiState.collectAsStateWithLifecycle().value
    DeleteModelScreen(
        modifier = modifier,
        state = state.languagesState,
        snackBarMessage = snackBarMessage,
        onCheckClicked = vm::onCheckClicked,
        onDeleteClicked = vm::onDeleteClicked,
        navigateToTranslation = navigateToTranslation,
        onBackClicked = onBackClicked,
    )
}

@Composable
private fun DeleteModelScreen(
    modifier: Modifier = Modifier,
    state: List<EachLanguageState>,
    snackBarMessage: MutableState<String>,
    onCheckClicked: (Locale) -> Unit,
    onDeleteClicked: (List<Locale>, () -> Unit) -> Unit,
    navigateToTranslation: () -> Unit,
    onBackClicked: (() -> Unit)?,
) {
    Scaffold(
        topBar = {
            TopBar(
                title = stringResource(id = R.string.topbar_lbl_delete_model),
                showTrailingIcon = false,
                onAddModelClicked = {},
                onDeleteModelClicked = {},
                onInquiryClicked = {},
                onBackClicked = onBackClicked,
            )
        },
    ) { innerPadding ->
        Column(
            modifier =
            modifier
                .fillMaxSize()
                .padding(innerPadding),
        ) {
            Text(
                text = stringResource(id = R.string.feature_delete_models_choose_language_desc),
                style = MaterialTheme.typography.headlineSmall,
            )
            Text(
                text = stringResource(id = R.string.feature_delete_models_re_download_desc),
                style = MaterialTheme.typography.bodyMedium,
            )
            Spacer(modifier = Modifier.height(12.dp))

            LanguageList(
                modifier = Modifier.weight(1f),
                locales = state.map { it.locale },
                state = state,
                onCheckClicked = onCheckClicked,
            )

            val showConfirmDialog = remember { mutableStateOf(false) }
            DangerActionButton(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                onClick = {
                    showConfirmDialog.value = true
                },
            ) {
                Text(text = stringResource(id = R.string.feature_delete_models_delete))
            }

            if (showConfirmDialog.value) {
                val snackBarResourceMessage =
                    stringResource(id = R.string.feature_delete_models_delete_success)
                ConfirmDialog(
                    showConfirmDialog = showConfirmDialog,
                    dialogText = stringResource(id = R.string.feature_delete_models_confirm_delete),
                    confirmButtonText = R.string.feature_delete_models_delete,
                    dismissButtonText = R.string.feature_delete_models_cancel,
                    onConfirmClicked = {
                        onDeleteClicked(
                            state.filter { it.checked.value }.map { it.locale },
                            navigateToTranslation,
                        )
                        snackBarMessage.value = snackBarResourceMessage
                    },
                )
            }
        }
    }
}

@Preview
@Composable
fun DeleteModelScreenPreview() {
    DeleteModelScreen(
        state =
        listOf(
            EachLanguageState(
                locale = Locale.GERMAN,
                checked = remember { mutableStateOf(false) },
                downloaded = null,
            ),
            EachLanguageState(
                locale = Locale.CHINESE,
                checked = remember { mutableStateOf(true) },
                downloaded = null,
            ),
        ),
        snackBarMessage = remember { mutableStateOf("") },
        onCheckClicked = {},
        onDeleteClicked = { _, _ -> },
        navigateToTranslation = {},
        onBackClicked = {},
    )
}
