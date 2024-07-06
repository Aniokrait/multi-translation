package io.github.aniokrait.multitranslation.ui.screen.initialsetting

import android.content.Context
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import io.github.aniokrait.multitranslation.R

/**
 * The dialog to confirm downloading translation models when the device is not connected to Wi-Fi.
 * @param showConfirmDownloadDialog Flag to show the dialog.
 * @param context Context is needed to download models.
 * @param navigateToTranslation Navigate to Translation screen.
 * @param onProceedClicked Lambda executed when the user clicks the Proceed button.
 * @param errorMessageTemplate First message showed in snackBar.
 * @param snackBarMessage Message to show in snackBar following to errorMessageTemplate.
 */
@Composable
fun DownloadConfirmDialog(
    showConfirmDownloadDialog: MutableState<Boolean>,
    context: Context,
    navigateToTranslation: () -> Unit,
    onProceedClicked: (Context, () -> Unit, String, MutableState<String>) -> Unit,
    errorMessageTemplate: String,
    snackBarMessage: MutableState<String>,
) {
    AlertDialog(
        text = {
            Text(text = stringResource(id = R.string.dialog_content))
        },
        onDismissRequest = { showConfirmDownloadDialog.value = false },
        confirmButton = {
            Button(onClick = {
                onProceedClicked(
                    context,
                    navigateToTranslation,
                    errorMessageTemplate,
                    snackBarMessage
                )
                showConfirmDownloadDialog.value = false
            }) {
                Text(text = stringResource(id = R.string.dialog_btn_proceed))
            }
        },
        dismissButton = {
            OutlinedButton(onClick = { showConfirmDownloadDialog.value = false }) {
                Text(text = stringResource(id = R.string.dialog_btn_cancel))
            }
        }
    )
}

@Preview
@Composable
private fun DownloadConfirmDialogPreview() {
    DownloadConfirmDialog(
        showConfirmDownloadDialog = remember { mutableStateOf(false) },
        context = LocalContext.current,
        navigateToTranslation = {},
        onProceedClicked = { _, _, _, _ -> },
        errorMessageTemplate = "",
        snackBarMessage = remember { mutableStateOf("") }
    )
}
