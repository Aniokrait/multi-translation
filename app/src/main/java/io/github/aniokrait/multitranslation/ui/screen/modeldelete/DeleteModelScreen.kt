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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.github.aniokrait.multitranslation.R
import io.github.aniokrait.multitranslation.ui.TopBar
import io.github.aniokrait.multitranslation.ui.screen.common.DangerActionButton
import io.github.aniokrait.multitranslation.ui.screen.common.LanguageList
import io.github.aniokrait.multitranslation.ui.stateholder.EachLanguageState
import java.util.Locale

@Composable
fun DeleteModelScreen(
//    vm: DeleteModelViewModel
) {
    DeleteModelScreen()
}

@Composable
private fun DeleteModelScreen(
    modifier: Modifier = Modifier,
    state: List<EachLanguageState>,
    onCheckClicked: (Locale) -> Unit,
    onDeleteClicked: () -> Unit,
    onBackClicked: (() -> Unit),
) {
    Scaffold(
        topBar = {
            TopBar(
                onBackClicked = onBackClicked,
            )
        },
    ) { innerPadding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Text(
                text = stringResource(id = R.string.lbl_description_for_delete_models),
                style = MaterialTheme.typography.headlineSmall,
            )
            Text(
                text = stringResource(id = R.string.lbl_delete_hosoku),
                style = MaterialTheme.typography.bodyMedium
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
                Text(text = stringResource(id = R.string.btn_delete))
            }

            if (showConfirmDialog.value) {
                DeleteConfirmDialog(
                    showConfirmDialog = showConfirmDialog,
                    onDeleteClicked = onDeleteClicked,
                )
            }
        }
    }
}

@Preview
@Composable
fun DeleteModelScreenPreview() {
    DeleteModelScreen(
        state = listOf(
            EachLanguageState(
                locale = Locale.GERMAN,
                checked = remember { mutableStateOf(false) },
                downloaded = null
            ),
            EachLanguageState(
                locale = Locale.CHINESE,
                checked = remember { mutableStateOf(true) },
                downloaded = null
            )
        ),
        onCheckClicked = {},
        onDeleteClicked = {},
        onBackClicked = {},
    )
}
