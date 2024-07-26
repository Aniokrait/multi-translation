package io.github.aniokrait.multitranslation.viewmodel

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.aniokrait.multitranslation.repository.LanguageModelRepository
import io.github.aniokrait.multitranslation.ui.stateholder.EachLanguageState
import io.github.aniokrait.multitranslation.ui.stateholder.uistate.DeleteModelUiState
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.Locale

@OptIn(ExperimentalCoroutinesApi::class)
class DeleteModelViewModel(
    private val repository: LanguageModelRepository,
) : ViewModel() {
    private val retryEvent: MutableStateFlow<RetryEvent> = MutableStateFlow(RetryEvent.Retrying)

    private val checkStateFlow: StateFlow<Map<Locale, MutableState<Boolean>>> = retryEvent
        .filter { it == RetryEvent.Retrying }
        .flatMapLatest { initCheckState() }
        .onEach {
            retryEvent.value = RetryEvent.Idle
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = mapOf()
        )

    private val isDeleting: MutableStateFlow<Boolean> = MutableStateFlow(false)

    val uiState: StateFlow<DeleteModelUiState> =
        combine(
            checkStateFlow,
            isDeleting,
        ) { checkState, isDeleting ->
            val languageState = checkState
                .filter { it.key != Locale.ENGLISH }
                .map {
                    EachLanguageState(
                        locale = it.key,
                        downloaded = null,
                        checked = checkState[it.key] ?: mutableStateOf(false)
                    )
                }.sortedBy { it.locale.displayLanguage }

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
                val checked =
                    checkStateFlow.value[Locale.forLanguageTag(it.language)]?.value ?: false
                Locale.forLanguageTag(it.language) to mutableStateOf(checked)
            }
        emit(downloadedModels)
    }

    /**
     * Delete language translation models.
     *
     * @param targetLanguages which to delete from the user device.
     */
    fun onDeleteClicked(targetLanguages: List<Locale>) {
        isDeleting.value = true

        viewModelScope.launch {
            repository.deleteModel(targetLanguages)
            isDeleting.value = false
            retryEvent.value = RetryEvent.Retrying
        }
    }

    /**
     * Update checked state.
     *
     * @param locale which to update checked state.
     */
    fun onCheckClicked(locale: Locale) {
        checkStateFlow.value[locale]?.value = checkStateFlow.value[locale]?.value != true
    }
}

private sealed interface RetryEvent {
    data object Idle : RetryEvent
    data object Retrying : RetryEvent
}
