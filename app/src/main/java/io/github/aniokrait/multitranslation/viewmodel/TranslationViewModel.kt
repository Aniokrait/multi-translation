package io.github.aniokrait.multitranslation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.mlkit.common.model.RemoteModelManager
import com.google.mlkit.nl.translate.TranslateLanguage
import com.google.mlkit.nl.translate.TranslateRemoteModel
import com.google.mlkit.nl.translate.Translation
import com.google.mlkit.nl.translate.TranslatorOptions
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.util.Locale

/**
 * ViewModel for initial download screen.
 * Context is injected by Koin as Application context, so we are suppressing the warning.
 */
class TranslationViewModel : ViewModel() {
    private val _translationResultFlow: MutableStateFlow<Map<String, String>> =
        getDownloadedLanguages()

    val translationResultFlow: StateFlow<Map<String, String>> = _translationResultFlow

    private fun getDownloadedLanguages(): MutableStateFlow<Map<String, String>> {
        viewModelScope.launch {
            val downloadedModelsTask =
                RemoteModelManager.getInstance()
                    .getDownloadedModels(TranslateRemoteModel::class.java)
            val downloadedModels = downloadedModelsTask.await()

            viewModelScope.launch {
                _translationResultFlow.emit(
                    downloadedModels.associate {
                        Locale.forLanguageTag(it.language).displayName to ""
                    }
                )
            }
        }

        return MutableStateFlow(mapOf())
    }

    fun onTranslateClick(
        input: String,
    ) {
        viewModelScope.launch {
            val downloadedLanguages = getDownloadedLanguages().value.keys.toList()

            val translationResults = mutableMapOf<String, String>()
            downloadedLanguages.forEach {
                val targetLocale = Locale.forLanguageTag(it)

                // TODO: Download if model needed to guard not exist model unexpectedly.

                val options = TranslatorOptions.Builder()
                    .setSourceLanguage(TranslateLanguage.JAPANESE)
                    .setTargetLanguage(targetLocale.toLanguageTag())
                    .build()
                val japaneseToOtherTranslator = Translation.getClient(options)

                val result = japaneseToOtherTranslator.translate(input).await()

                translationResults[targetLocale.getDisplayName(Locale.getDefault())] = result
            }

            _translationResultFlow.value = translationResults
        }
    }

}
