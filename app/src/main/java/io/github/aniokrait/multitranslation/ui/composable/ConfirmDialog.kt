package io.github.aniokrait.multitranslation.ui.composable

import androidx.annotation.StringRes
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import io.github.aniokrait.multitranslation.R

/**
 * Dialog to confirm some action.
 * @param showConfirmDialog Flag to show the dialog.
 * @param dialogText Text showed in the dialog.
 * @param confirmButtonText Confirm button text.
 * @param dismissButtonText Dismiss button text.
 * @param onConfirmClicked Lambda executed when the user clicks the Confirm button.
 */
@Composable
fun ConfirmDialog(
    showConfirmDialog: MutableState<Boolean>,
    dialogText: String,
    @StringRes confirmButtonText: Int,
    @StringRes dismissButtonText: Int,
    onConfirmClicked: () -> Unit,
) {
    AlertDialog(
        text = {
            Text(text = dialogText)
        },
        onDismissRequest = { showConfirmDialog.value = false },
        confirmButton = {
            Button(onClick = {
                onConfirmClicked()
                showConfirmDialog.value = false
            }) {
                Text(text = stringResource(id = confirmButtonText))
            }
        },
        dismissButton = {
            OutlinedButton(onClick = { showConfirmDialog.value = false }) {
                Text(text = stringResource(id = dismissButtonText))
            }
        }
    )
}

@Preview
@Composable
private fun ConfirmDialogPreview() {
    ConfirmDialog(
        showConfirmDialog = remember { mutableStateOf(false) },
        dialogText = "翻訳モデルを削除しますか？",
        confirmButtonText = R.string.btn_delete,
        dismissButtonText = R.string.delete_dialog_btn_cancel,
        onConfirmClicked = {}
    )
}
