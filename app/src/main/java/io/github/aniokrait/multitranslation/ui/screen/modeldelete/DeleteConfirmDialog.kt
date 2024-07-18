package io.github.aniokrait.multitranslation.ui.screen.modeldelete

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

@Composable
fun DeleteConfirmDialog(
    showConfirmDialog: MutableState<Boolean>,
    onDeleteClicked: () -> Unit,
) {
    // TODO: Make it common with DownloadConfirmDialog.
    AlertDialog(
        text = {
            Text(text = stringResource(id = R.string.lbl_delete_model))
        },
        onDismissRequest = { showConfirmDialog.value = false },
        confirmButton = {
            Button(onClick = {
                onDeleteClicked()
                showConfirmDialog.value = false
            }) {
                Text(text = stringResource(id = R.string.btn_delete))
            }
        },
        dismissButton = {
            OutlinedButton(onClick = { showConfirmDialog.value = false }) {
                Text(text = stringResource(id = R.string.delete_dialog_btn_cancel))
            }
        }
    )
}

@Preview
@Composable
private fun DeleteConfirmDialogPreview() {
    DeleteConfirmDialog(
        showConfirmDialog = remember { mutableStateOf(false) },
        onDeleteClicked = {},
    )
}
