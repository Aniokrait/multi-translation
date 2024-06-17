package io.github.aniokrait.multitranslation.ui.stateholder

import androidx.compose.runtime.MutableState
import java.util.Locale

data class DownloadedState(
    val locale: Locale,
    val downloaded: MutableState<Boolean>,
)
