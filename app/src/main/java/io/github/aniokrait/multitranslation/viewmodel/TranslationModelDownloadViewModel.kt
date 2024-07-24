package io.github.aniokrait.multitranslation.viewmodel

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.aniokrait.multitranslation.core.LanguageNameResolver
import io.github.aniokrait.multitranslation.repository.LanguageModelRepository
import io.github.aniokrait.multitranslation.ui.stateholder.EachLanguageState
import io.github.aniokrait.multitranslation.ui.stateholder.uistate.TranslationModelDownloadUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.Locale

class TranslationModelDownloadViewModel(
    private val repository: LanguageModelRepository,
) : ViewModel() {

    private val checkState: StateFlow<Map<Locale, MutableState<Boolean>>> = initCheckState()
    private val isDownloading: MutableStateFlow<Boolean> = MutableStateFlow(false)
    private val allDownloadFailed: MutableStateFlow<Boolean> = MutableStateFlow(false)

    val uiState: StateFlow<TranslationModelDownloadUiState> =
        combine(
            repository.getDownloadedInfo(),
            checkState,
            isDownloading,
            allDownloadFailed,
        ) { downloadedInfo, checkState, isDownloading, allDownloadFailed ->
            val list = mutableListOf<EachLanguageState>()
            for (info in downloadedInfo) {
                val locale = info.locale
                val eachState = EachLanguageState(
                    locale = locale,
                    downloaded = info.downloaded,
                    checked = checkState[locale] ?: mutableStateOf(false)
                )

                list.add(eachState)
            }

            TranslationModelDownloadUiState(
                languagesState = list,
                isDownloading = isDownloading,
                allDownloadFailed = allDownloadFailed,
            )
        }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(),
                initialValue = TranslationModelDownloadUiState()
            )

    /**
     * Update checked state.
     *
     * @param locale which to update checked state.
     */
    fun onCheckClicked(locale: Locale) {
        checkState.value[locale]?.value = checkState.value[locale]?.value != true
    }

    /**
     * Download models.
     * Proceed to Translation screen, when all downloads completed
     */
    fun onDownloadClicked(
        navigateToTranslation: () -> Unit,
        errorMessageTemplate: String,
        snackBarMessageState: MutableState<String>,
    ) {
        isDownloading.value = true

        val checkedLanguages: List<Locale> = checkState.value
            // filter map.value(MutableState).value
            .filter { it.value.value }
            // convert set to list
            .keys.map { it }
        viewModelScope.launch {
            repository.downloadModel(
                targetLanguages = checkedLanguages,
            )
            isDownloading.value = false

//            if (checkedLanguages.size == failedModels.size) {
//                allDownloadFailed.value = true
//                return@launch
//            }
//
//            if (failedModels.isNotEmpty()) {
//                // Set value like this.
//                /* 以下の言語のダウンロードに失敗しました。
//                   ・日本語
//                   ・英語
//                */
//                snackBarMessageState.value = failedModels
//                    .fold(errorMessageTemplate) { acc, locale -> "$acc\n・${locale.displayName}" }
//            }
            navigateToTranslation()
        }
    }

    /**
     * Reset the all download failed flag.
     */
    fun onDownloadFailedDialogOkClicked() {
        allDownloadFailed.value = false
    }

    private fun initCheckState(): StateFlow<Map<Locale, MutableState<Boolean>>> {

        return MutableStateFlow(
            LanguageNameResolver.getAllLanguagesLabel()
                .associateWith { mutableStateOf(false) }
        )
    }
}
