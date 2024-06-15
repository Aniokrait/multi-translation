package io.github.aniokrait.multitranslation.repository

import io.github.aniokrait.multitranslation.ui.stateholder.LanguageDownloadState.EachLanguageState
import kotlinx.coroutines.flow.Flow

interface LanguageModelRepository {
    fun getDownloadedInfo(): Flow<List<EachLanguageState>>
}
