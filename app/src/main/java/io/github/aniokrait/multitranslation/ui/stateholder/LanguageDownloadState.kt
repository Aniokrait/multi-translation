package io.github.aniokrait.multitranslation.ui.stateholder

import androidx.compose.runtime.MutableState
import java.util.Locale

data class LanguageDownloadState(
    val languagesState: List<EachLanguageState>,
) {
    data class EachLanguageState(
        val locale: Locale,
        val checked: MutableState<Boolean>,
        val downloaded: MutableState<Boolean>,
    )
}

