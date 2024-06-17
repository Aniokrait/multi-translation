package io.github.aniokrait.multitranslation.repository

import io.github.aniokrait.multitranslation.ui.stateholder.DownloadedState
import kotlinx.coroutines.flow.Flow

interface LanguageModelRepository {
    fun getDownloadedInfo(): Flow<List<DownloadedState>>
//    fun checkLanguage(locale: Locale)

    fun getSimpleState(): Flow<Int>
    fun updateSimpleState()
}
