package io.github.aniokrait.multitranslation.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import io.github.aniokrait.multitranslation.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(
    title: String = stringResource(id = R.string.app_name),
    // TODO: Think about the ways to handle the increasing args
    showTrailingIcon: Boolean = true,
    onAddModelClicked: () -> Unit,
    onDeleteModelClicked: () -> Unit,
    onInquiryClicked: () -> Unit,
    onBackClicked: (() -> Unit)? = null,
) {
    TopAppBar(
        title = { Text(text = title) },
        actions = {
            if (showTrailingIcon) {
                Box {
                    var expanded by remember { mutableStateOf(false) }

                    IconButton(onClick = { expanded = true }) {
                        Icon(Icons.Filled.Settings, contentDescription = "settings")
                    }

                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false },
                    ) {
                        DropdownMenuItem(
                            text = { Text(text = stringResource(id = R.string.topbar_lbl_add_model)) },
                            onClick = onAddModelClicked,
                        )

                        DropdownMenuItem(
                            text = { Text(text = stringResource(id = R.string.topbar_lbl_delete_model)) },
                            onClick = onDeleteModelClicked,
                        )

                        DropdownMenuItem(
                            text = { Text(text = stringResource(id = R.string.topbar_lbl_inquiry)) },
                            onClick = onInquiryClicked,
                        )
                    }
                }

            }
        },
        navigationIcon = {
            if (onBackClicked != null) {
                IconButton(onClick = onBackClicked) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "back")
                }
            }
        },
    )
}

@Composable
@Preview
private fun TopBarPreview() {
    TopBar(
        showTrailingIcon = true,
        onAddModelClicked = {},
        onDeleteModelClicked = {},
        onBackClicked = {},
        onInquiryClicked = {},
    )
}
