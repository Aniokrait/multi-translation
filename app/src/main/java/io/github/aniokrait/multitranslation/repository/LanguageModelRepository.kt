package io.github.aniokrait.multitranslation.repository

import io.github.aniokrait.multitranslation.ui.stateholder.LanguageDownloadState.EachLanguageState
import kotlinx.coroutines.flow.Flow
import java.util.Locale

interface LanguageModelRepository {
    fun getDownloadedInfo(): Flow<List<EachLanguageState>>
    fun checkLanguage(locale: Locale)

    fun getSimpleState(): Flow<Int>
    fun updateSimpleState()
}
