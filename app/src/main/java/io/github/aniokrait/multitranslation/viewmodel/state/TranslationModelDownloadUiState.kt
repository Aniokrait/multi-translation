package io.github.aniokrait.multitranslation.viewmodel.state

import androidx.compose.runtime.MutableState
import java.util.Locale

data class TranslationModelDownloadUiState(
    val languagesState: List<EachLanguageState> = listOf(),
    val isDownloading: Boolean = false,
    val allDownloadFailed: Boolean = false,
) {
    data class EachLanguageState(
        val locale: Locale,
        val checked: MutableState<Boolean>,
        val downloaded: MutableState<Boolean>,
    )
}

