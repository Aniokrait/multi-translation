package io.github.aniokrait.multitranslation.viewmodel

import androidx.lifecycle.ViewModel

class TranslationViewModel: ViewModel() {
    private val _translationResult = mutableMapOf<String, String>()
    val translationResult: Map<String, String> get() = _translationResult

    fun onTranslateClick(input: String) {
        _translationResult["english"] = "123"
    }
}
