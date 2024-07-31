package io.github.aniokrait.multitranslation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.aniokrait.multitranslation.repository.HttpRequestResult
import io.github.aniokrait.multitranslation.repository.InquiryRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class InquiryViewModel(
    private val repository: InquiryRepository,
) : ViewModel() {

    private val _uiState: MutableStateFlow<InquiryUiState> =
        MutableStateFlow(InquiryUiState.Initial)
    val uiState: StateFlow<InquiryUiState> = _uiState.asStateFlow()

    /**
     * Send inquiry to Slack.
     *
     * @param content Inquiry content.
     */
    fun sendInquiry(content: String) {
        _uiState.value = InquiryUiState.Loading
        viewModelScope.launch {
            val result = repository.sendInquiry(content = content)
            _uiState.value = if (result is HttpRequestResult.Success) {
                InquiryUiState.SentSuccess
            } else {
                InquiryUiState.Failure
            }
        }
    }
}

sealed interface InquiryUiState {
    data object Initial : InquiryUiState
    data object Loading : InquiryUiState
    data object SentSuccess : InquiryUiState
    data object Failure : InquiryUiState
}
