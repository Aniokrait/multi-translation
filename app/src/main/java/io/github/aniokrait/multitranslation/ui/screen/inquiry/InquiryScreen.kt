package io.github.aniokrait.multitranslation.ui.screen.inquiry

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import io.github.aniokrait.multitranslation.viewmodel.InquiryViewModel
import kotlinx.serialization.Serializable
import org.koin.androidx.compose.koinViewModel

@Serializable
object Inquiry

@Composable
fun InquiryScreen(
    vm: InquiryViewModel = koinViewModel(),
) {
    Text(text = "WIP")
}

@Composable
private fun InquiryScreen() {

}

@Preview
@Composable
private fun InquiryScreenPreview() {
    InquiryScreen()
}
