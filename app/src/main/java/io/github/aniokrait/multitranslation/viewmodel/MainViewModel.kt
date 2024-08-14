package io.github.aniokrait.multitranslation.viewmodel

import androidx.lifecycle.ViewModel
import com.google.mlkit.nl.translate.TranslateLanguage
import io.github.aniokrait.multitranslation.repository.LanguageModelRepository

class MainViewModel(
    private val languageModelRepository: LanguageModelRepository,
) : ViewModel() {
    suspend fun checkIfFirstLaunch(): Boolean {
        return languageModelRepository.getDownloadedModels()
            .none { it.language != TranslateLanguage.ENGLISH }
    }
}
