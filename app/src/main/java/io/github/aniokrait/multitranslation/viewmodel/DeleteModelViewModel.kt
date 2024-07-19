package io.github.aniokrait.multitranslation.viewmodel

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.aniokrait.multitranslation.repository.LanguageModelRepository
import io.github.aniokrait.multitranslation.ui.stateholder.EachLanguageState
import io.github.aniokrait.multitranslation.ui.stateholder.uistate.DeleteModelUiState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.Locale

class DeleteModelViewModel(
    private val repository: LanguageModelRepository,
) : ViewModel() {
    val checkState: StateFlow<Map<Locale, MutableState<Boolean>>> =
        initCheckState()
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(),
                initialValue = mapOf()
            )

    private val isDeleting: MutableStateFlow<Boolean> = MutableStateFlow(false)

    val uiState: StateFlow<DeleteModelUiState> =
        combine(
            checkState,
            isDeleting,
        ) { checkState, isDeleting ->
            val languageState = checkState.map {
                EachLanguageState(
                    locale = it.key,
                    downloaded = null,
                    checked = checkState[it.key] ?: mutableStateOf(false)
                )
            }

            DeleteModelUiState(
                languagesState = languageState
            )
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = DeleteModelUiState()
        )

    private fun initCheckState(): Flow<Map<Locale, MutableState<Boolean>>> = flow {
        val downloadedModels: Map<Locale, MutableState<Boolean>> =
            repository.getDownloadedModels().associate {
                Locale.forLanguageTag(it.language) to mutableStateOf(false)
            }
        emit(downloadedModels)
    }

    /**
     * Delete language translation models.
     *
     * @param targetLanguages which to delete from the user device.
     */
    fun deleteModels(targetLanguages: List<Locale>) {
        viewModelScope.launch {
            isDeleting.value = true
            repository.deleteModel(targetLanguages)
            isDeleting.value = false
        }
    }
}
