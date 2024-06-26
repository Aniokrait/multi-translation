package io.github.aniokrait.multitranslation.viewmodel

import android.content.Context
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.aniokrait.multitranslation.core.LanguageNameResolver
import io.github.aniokrait.multitranslation.repository.LanguageModelRepository
import io.github.aniokrait.multitranslation.viewmodel.state.InitialDownloadViewModelState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.Locale

class InitialDownloadViewModel(
    private val repository: LanguageModelRepository,
) : ViewModel() {

    private val checkState: StateFlow<Map<Locale, MutableState<Boolean>>> = initCheckState()

    val downloadState: StateFlow<InitialDownloadViewModelState> =
        repository.getDownloadedInfo().combine(checkState) { downloadedInfo, checkState ->
            val list = mutableListOf<InitialDownloadViewModelState.EachLanguageState>()
            for (info in downloadedInfo) {
                val locale = info.locale
                val eachState = InitialDownloadViewModelState.EachLanguageState(
                    locale = locale,
                    downloaded = info.downloaded,
                    checked = checkState[locale] ?: mutableStateOf(false)
                )

                list.add(eachState)
            }

            InitialDownloadViewModelState(languagesState = list)
        }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(),
                initialValue = InitialDownloadViewModelState()
            )

    fun onCheckClicked(locale: Locale) {
        checkState.value[locale]?.value = checkState.value[locale]?.value != true
    }

    /**
     * Download models.
     * Proceed to Translation screen, when all downloads completed
     */
    fun onDownloadClicked(
        context: Context,
        navigateToTranslation: () -> Unit,
    ) {
        val checkedLanguages = checkState.value
            // filter map.value(MutableState).value
            .filter { it.value.value }
            // convert set to list
            .keys.map { it }
        viewModelScope.launch {
            repository.downloadModel(
                targetLanguages = checkedLanguages,
                context = context,
            )

            navigateToTranslation()
        }

    }

    private fun initCheckState(): StateFlow<Map<Locale, MutableState<Boolean>>> {

        return MutableStateFlow(
            LanguageNameResolver.getAllLanguagesLabel()
                .associateWith { mutableStateOf(false) }
        )
    }
}
