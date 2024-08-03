package io.github.aniokrait.multitranslation.extension.ui

import androidx.compose.ui.Modifier

/**
 * Conditional modifier.
 */
fun Modifier.conditional(
    condition: Boolean,
    modifier: Modifier.() -> Modifier,
): Modifier {
    return if (condition) {
        then(modifier(Modifier))
    } else {
        this
    }
}
