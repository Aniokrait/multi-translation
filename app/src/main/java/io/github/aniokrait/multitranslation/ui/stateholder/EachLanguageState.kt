package io.github.aniokrait.multitranslation.ui.stateholder

import androidx.compose.runtime.MutableState
import java.util.Locale

/**
 * Each language state.
 * Property "downloaded" is nullable because it is not needed in the DeleteModelScreen.
 *
 * @property locale Language locale.
 * @property checked Checked state.
 * @property downloaded Downloaded state.
 */
data class EachLanguageState(
    val locale: Locale,
    val checked: MutableState<Boolean>,
    val downloaded: MutableState<Boolean>?,
)
