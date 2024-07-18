package io.github.aniokrait.multitranslation.ui.stateholder.uistate

import io.github.aniokrait.multitranslation.ui.stateholder.EachLanguageState

data class TranslationModelDownloadUiState(
    val languagesState: List<EachLanguageState> = listOf(),
    val isDownloading: Boolean = false,
    val allDownloadFailed: Boolean = false,
)

