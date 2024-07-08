package io.github.aniokrait.multitranslation.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import io.github.aniokrait.multitranslation.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(
    // TODO: Think about the ways to handle the increasing args
    onSettingsClick: (() -> Unit)? = null,
    onBackClicked: (() -> Unit)? = null,
) {
    TopAppBar(
        title = { Text(text = stringResource(id = R.string.app_name)) },
        actions = {
            if (onSettingsClick != null) {
                IconButton(onClick = onSettingsClick) {
                    Icon(Icons.Filled.Settings, contentDescription = "settings")
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
        onSettingsClick = {},
        onBackClicked = {},
    )
}
