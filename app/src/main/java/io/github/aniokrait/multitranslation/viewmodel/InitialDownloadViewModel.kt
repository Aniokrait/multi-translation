package io.github.aniokrait.multitranslation.viewmodel

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.aniokrait.multitranslation.core.LanguageNameResolver
import io.github.aniokrait.multitranslation.repository.LanguageModelRepository
import io.github.aniokrait.multitranslation.ui.stateholder.InitialDownloadScreenState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import java.util.Locale

class InitialDownloadViewModel(
    private val repository: LanguageModelRepository,
) : ViewModel() {

    private val checkState: StateFlow<Map<Locale, MutableState<Boolean>>> = initCheckState()

    val downloadState: StateFlow<List<InitialDownloadScreenState.EachLanguageState>> =
        repository.getDownloadedInfo().combine(checkState) { downloadedInfo, checkState ->
            val list = mutableListOf<InitialDownloadScreenState.EachLanguageState>()
            for (info in downloadedInfo) {
                val locale = info.locale
                val eachState = InitialDownloadScreenState.EachLanguageState(
                    locale = locale,
                    downloaded = info.downloaded,
                    checked = checkState[locale] ?: mutableStateOf(false)
                )

                list.add(eachState)
            }

            list
        }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(),
                initialValue = listOf()
            )

    fun onCheckClicked(locale: Locale) {
        checkState.value[locale]?.value = checkState.value[locale]?.value != true
    }

    fun updateSimpleState() {
        repository.updateSimpleState()
    }

    private fun initCheckState(): StateFlow<Map<Locale, MutableState<Boolean>>> {

        return MutableStateFlow(
            LanguageNameResolver.getAllLanguagesLabel()
                .associateWith { mutableStateOf(false) }
        )
    }
}
