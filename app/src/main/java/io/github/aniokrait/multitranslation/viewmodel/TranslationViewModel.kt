package io.github.aniokrait.multitranslation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.mlkit.nl.translate.Translation
import com.google.mlkit.nl.translate.TranslatorOptions
import io.github.aniokrait.multitranslation.repository.LanguageModelRepository
import io.github.aniokrait.multitranslation.ui.stateholder.uistate.TranslationUiState
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapMerge
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.util.Locale

/**
 * ViewModel for initial download screen.
 * Context is injected by Koin as Application context, so we are suppressing the warning.
 */
@OptIn(ExperimentalCoroutinesApi::class)
class TranslationViewModel(
    private val repository: LanguageModelRepository,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO,
    private val mainDispatcher: CoroutineDispatcher = Dispatchers.Main,
) : ViewModel() {
    // Store partial languages translated results.
    private val partialTranslatedResults: StateFlow<MutableMap<Locale, String>> =
        initTranslationResults()
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(),
                initialValue = mutableMapOf(),
            )

    // Store each language translated results.
    private val eachTranslatedResult: MutableStateFlow<Pair<Locale, String>> =
        MutableStateFlow(
            Pair(
                Locale.getDefault(),
                "",
            ),
        )

    // Store all languages translated results.
    private val allTranslatedResults =
        combine(
            partialTranslatedResults,
            eachTranslatedResult.flatMapMerge { MutableStateFlow(it) },
        ) { translationResult, eachTranslatedResult ->
            translationResult[eachTranslatedResult.first] = eachTranslatedResult.second
            translationResult
        }

    private val isTranslating: MutableStateFlow<Boolean> = MutableStateFlow(false)

    private val sourceLanguage: MutableStateFlow<Locale> = MutableStateFlow(Locale.getDefault())

    val uiState: StateFlow<TranslationUiState> =
        combine(
            isTranslating,
            allTranslatedResults,
            sourceLanguage,
        ) { isTranslating, allTranslatedResults, sourceLanguage ->

            TranslationUiState(
                translationResult =
                allTranslatedResults
                    .filter { it.key.language != sourceLanguage.language && it.key.country.isEmpty() }
                    .toMap(),
                isTranslating = isTranslating,
                sourceLanguage = sourceLanguage,
            )
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = TranslationUiState(),
        )

    private fun initTranslationResults(): Flow<MutableMap<Locale, String>> =
        flow {
            val downloadedModels = repository.getDownloadedModels()

            emit(
                downloadedModels.associate {
                    Locale.forLanguageTag(it.language) to ""
                }.toSortedMap(compareBy { it.displayLanguage })
                    .toMutableMap(),
            )
        }

    /**
     * Translate from input text to other languages.
     */
    fun onTranslateClick(input: String) {
        isTranslating.value = true
        viewModelScope.launch(ioDispatcher) {
            val downloadedLanguages = uiState.value.translationResult.keys.toList()

            downloadedLanguages
                .filter {
                    it.language.substring(0, 2) != sourceLanguage.value
                        .language.substring(0, 2)
                }
                .forEach { targetLocale ->
                    Timber.d("targetLocale: $targetLocale")
                    sourceLanguage.value.language

                    withContext(ioDispatcher) {
                        val options =
                            TranslatorOptions.Builder()
                                .setSourceLanguage(
                                    sourceLanguage.value.toLanguageTag()
                                        .substring(0, 2) // Trim en_US to en.
                                )
                                .setTargetLanguage(
                                    targetLocale.toLanguageTag().substring(0, 2)
                                )  // Trim en_US to en.
                                .build()
                        val translator = Translation.getClient(options)

                        // Guard against not exist model.
                        val modelDownloadTask = translator.downloadModelIfNeeded()

                        // To translate parallel, launch coroutine for each language.
                        launch {
                            modelDownloadTask.await()

                            val translateResult =
                                Pair(
                                    targetLocale,
                                    translator.translate(input).await(),
                                )
                            translator.close()
                            withContext(mainDispatcher) {
                                eachTranslatedResult.emit(
                                    translateResult,
                                )
                            }
                        }
                    }
                }

            withContext(mainDispatcher) {
                isTranslating.value = false
            }
        }
    }

    fun onSourceLanguageClick(locale: Locale) {
        sourceLanguage.value = locale
    }
}
