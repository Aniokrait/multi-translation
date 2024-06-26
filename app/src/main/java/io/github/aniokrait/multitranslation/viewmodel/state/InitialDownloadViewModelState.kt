package io.github.aniokrait.multitranslation.viewmodel.state

import androidx.compose.runtime.MutableState
import java.util.Locale

data class InitialDownloadViewModelState(
    val languagesState: List<EachLanguageState> = listOf(),
) {
    data class EachLanguageState(
        val locale: Locale,
        val checked: MutableState<Boolean>,
        val downloaded: MutableState<Boolean>,
    )
}

