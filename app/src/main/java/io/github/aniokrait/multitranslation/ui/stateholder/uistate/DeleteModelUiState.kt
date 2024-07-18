package io.github.aniokrait.multitranslation.ui.stateholder.uistate

import io.github.aniokrait.multitranslation.ui.stateholder.EachLanguageState

data class DeleteModelUiState(
    val languagesState: List<EachLanguageState> = listOf(),
)
