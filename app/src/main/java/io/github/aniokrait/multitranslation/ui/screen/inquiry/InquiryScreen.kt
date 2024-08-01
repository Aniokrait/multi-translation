package io.github.aniokrait.multitranslation.ui.screen.inquiry

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.relocation.BringIntoViewRequester
import androidx.compose.foundation.relocation.bringIntoViewRequester
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.github.aniokrait.multitranslation.R
import io.github.aniokrait.multitranslation.ui.TopBar
import io.github.aniokrait.multitranslation.viewmodel.InquiryViewModel
import kotlinx.serialization.Serializable
import org.koin.androidx.compose.koinViewModel

@Serializable
object Inquiry

@Composable
fun InquiryScreen(
    vm: InquiryViewModel = koinViewModel(),
    onBackClicked: (() -> Unit),
) {
    InquiryScreen(
        onBackClicked = onBackClicked,
        onSentClicked = vm::sendInquiry
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun InquiryScreen(
    onBackClicked: (() -> Unit),
    onSentClicked: (String) -> Unit,
) {
    Scaffold(
        topBar = {
            TopBar(
                title = stringResource(id = R.string.topbar_lbl_inquiry),
                showTrailingIcon = false,
                onAddModelClicked = {},
                onDeleteModelClicked = {},
                onInquiryClicked = {},
                onBackClicked = onBackClicked,
            )
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)

        ) {
            Text(
                text = stringResource(id = R.string.lbl_inquiry_hosoku),
                style = MaterialTheme.typography.bodyMedium
            )

            val maxLength = 2000
            val bringIntoViewRequester = remember {
                BringIntoViewRequester()
            }

            Spacer(modifier = Modifier.height(12.dp))

            var content by remember { mutableStateOf("") }
            TextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .bringIntoViewRequester(bringIntoViewRequester)
                    .height(200.dp),
                value = content,
                onValueChange = {
                    if (it.length <= maxLength) {
                        content = it
                    }
                },
                maxLines = 20,
                supportingText = {
                    Text(
                        text = "${content.length} / $maxLength",
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.End,
                    )
                },

                )

            Spacer(
                modifier = Modifier.weight(1f)
            )
            Button(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                onClick = { onSentClicked(content) }
            ) {
                Text(text = stringResource(id = R.string.btn_inquiry))
            }
        }
    }
}

@Preview
@Composable
private fun InquiryScreenPreview() {
    InquiryScreen(
        onBackClicked = {},
        onSentClicked = {}
    )
}
