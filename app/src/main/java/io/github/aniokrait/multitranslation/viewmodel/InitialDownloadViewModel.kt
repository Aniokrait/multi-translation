package io.github.aniokrait.multitranslation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.aniokrait.multitranslation.repository.LanguageModelRepository
import io.github.aniokrait.multitranslation.ui.stateholder.LanguageDownloadState
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import java.util.Locale

class InitialDownloadViewModel(
    private val repository: LanguageModelRepository,
) : ViewModel() {

    val downloadState: StateFlow<List<LanguageDownloadState.EachLanguageState>> =
        repository.getDownloadedInfo()
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(),
                initialValue = listOf()
            )

    fun onCheckClicked(locale: Locale) {
        repository.checkLanguage(locale)
    }

    val simpleState: StateFlow<Int> = repository.getSimpleState()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = 0
        )

    fun updateSimpleState() {
        repository.updateSimpleState()
    }
}
