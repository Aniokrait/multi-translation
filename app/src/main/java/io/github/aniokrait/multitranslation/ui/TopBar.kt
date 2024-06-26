package io.github.aniokrait.multitranslation.ui

import androidx.compose.material.icons.Icons
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
    onSettingsClick: () -> Unit,
) {
    TopAppBar(
        title = { Text(text = stringResource(id = R.string.app_name))},
        actions = {
            IconButton(onClick = onSettingsClick) {
                Icon(Icons.Filled.Settings, contentDescription = "settings")
            }
        }
    )
}

@Composable
@Preview
private fun TopBarPreview() {
    TopBar(onSettingsClick = {},)
}
