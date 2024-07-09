package io.github.aniokrait.multitranslation.repository

import io.github.aniokrait.multitranslation.ui.stateholder.DownloadedState
import kotlinx.coroutines.flow.Flow
import java.util.Locale

interface LanguageModelRepository {
    fun getDownloadedInfo(): Flow<List<DownloadedState>>
//    fun checkLanguage(locale: Locale)

    fun getSimpleState(): Flow<Int>
    fun updateSimpleState()

    /**
     * Download language translation models.
     * @param targetLanguages Checked languages on selection screen.
     */
    suspend fun downloadModel(
        targetLanguages: List<Locale>,
    ): List<Locale>
}
