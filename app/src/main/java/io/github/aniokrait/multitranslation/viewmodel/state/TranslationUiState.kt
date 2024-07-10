package io.github.aniokrait.multitranslation.viewmodel.state

import java.util.Locale

data class TranslationUiState(
    val translationResult: Map<Locale, String> = mapOf(),
    val isTranslating: Boolean = false,
)
