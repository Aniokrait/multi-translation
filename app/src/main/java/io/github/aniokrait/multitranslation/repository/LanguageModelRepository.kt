package io.github.aniokrait.multitranslation.repository

import io.github.aniokrait.multitranslation.ui.stateholder.DownloadedState
import kotlinx.coroutines.flow.Flow
import java.util.Locale

interface LanguageModelRepository {
    fun getDownloadedInfo(): Flow<List<DownloadedState>>

    /**
     * Download language translation models.
     * @param targetLanguages Checked languages on selection screen.
     */
    suspend fun downloadModel(
        targetLanguages: List<Locale>,
    ): List<Locale>
}
