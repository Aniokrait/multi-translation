package io.github.aniokrait.multitranslation.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.mlkit.nl.translate.TranslateLanguage
import com.google.mlkit.nl.translate.Translation
import com.google.mlkit.nl.translate.TranslatorOptions
import io.github.aniokrait.multitranslation.datasource.dataStore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.util.Locale

class TranslationViewModel : ViewModel() {
    private val _translationResultFlow: MutableStateFlow<Map<String, String>> = MutableStateFlow(
        mutableMapOf()
    )
    val translationResultFlow: StateFlow<Map<String, String>> = _translationResultFlow

    fun onTranslateClick(
        input: String,
        context: Context,
    ) {
        viewModelScope.launch {
            val downloadedLanguages: List<String> = context.dataStore.data.map { preferences ->
                preferences.asMap().map {
                    it.key.name
                }
            }.first()

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
