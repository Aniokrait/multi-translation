package io.github.aniokrait.multitranslation.repository

import com.google.mlkit.nl.translate.TranslateRemoteModel
import io.github.aniokrait.multitranslation.ui.stateholder.DownloadedState
import kotlinx.coroutines.flow.Flow
import java.util.Locale

interface LanguageModelRepository {
    /**
     * Get downloaded models info and return DownloadState flow.
     */
    fun getDownloadedInfo(): Flow<List<DownloadedState>>

    /**
     * Get downloaded translation remote models.
     */
    suspend fun getDownloadedModels(): Set<TranslateRemoteModel>

    /**
     * Download language translation models.
     * @param targetLanguages Checked languages on selection screen.
     */
    suspend fun downloadModel(
        targetLanguages: List<Locale>,
    ): List<Locale>

    /**
     * Delete language translation models.
     * @param targetLanguages which to delete from the user device.
     */
    suspend fun deleteModel(targetLanguages: List<Locale>)
}
