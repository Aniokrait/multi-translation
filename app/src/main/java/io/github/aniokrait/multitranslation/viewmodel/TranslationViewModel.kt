package io.github.aniokrait.multitranslation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.mlkit.nl.translate.TranslateLanguage
import com.google.mlkit.nl.translate.Translation
import com.google.mlkit.nl.translate.TranslatorOptions
import io.github.aniokrait.multitranslation.repository.LanguageModelRepository
import io.github.aniokrait.multitranslation.viewmodel.state.TranslationUiState
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.util.Locale

/**
 * ViewModel for initial download screen.
 * Context is injected by Koin as Application context, so we are suppressing the warning.
 */
class TranslationViewModel(
    private val repository: LanguageModelRepository,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO,
    private val mainDispatcher: CoroutineDispatcher = Dispatchers.Main,
) : ViewModel() {
    companion object {
        val TAG: String = TranslationViewModel::class.java.simpleName
    }

    private val translationResultFlow: MutableStateFlow<Map<Locale, String>> =
        getDownloadedLanguages()
    private val isTranslating: MutableStateFlow<Boolean> = MutableStateFlow(false)

    val uiState: StateFlow<TranslationUiState> =
        combine(translationResultFlow, isTranslating) { translationResult, isTranslating ->
            TranslationUiState(
                translationResult = translationResult,
                isTranslating = isTranslating,
            )
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = TranslationUiState()
        )

    private fun getDownloadedLanguages(): MutableStateFlow<Map<Locale, String>> {
        viewModelScope.launch {
            val downloadedModels = repository.getDownloadedModels()

            translationResultFlow.emit(
                downloadedModels.associate {
                    Locale.forLanguageTag(it.language) to ""
                }
            )
        }

        return MutableStateFlow(mapOf())
    }

    fun onTranslateClick(
        input: String,
    ) {
        isTranslating.value = true
        viewModelScope.launch(ioDispatcher) {
            val downloadedLanguages = uiState.value.translationResult.keys.toList()

            val translationResults = mutableMapOf<Locale, String>()
            downloadedLanguages.forEach { targetLocale ->
                Log.d(TAG, "targetLocale: $targetLocale")

                // TODO: Download if model needed to guard not exist model unexpectedly.

                val result = withContext(ioDispatcher) {
                    val options = TranslatorOptions.Builder()
                        .setSourceLanguage(TranslateLanguage.JAPANESE)
                        .setTargetLanguage(targetLocale.language)
                        .build()
                    val japaneseToOtherTranslator = Translation.getClient(options)

                    return@withContext japaneseToOtherTranslator.translate(input).await()
                }

                translationResults[targetLocale] = result
            }

            withContext(mainDispatcher) {
                translationResultFlow.value = translationResults
                isTranslating.value = false
            }

        }
    }

}
