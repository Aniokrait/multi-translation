package io.github.aniokrait.multitranslation.repository

import android.content.Context
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
     * @param context Context is needed to store download result to DataStore.
     */
    suspend fun downloadModel(
        targetLanguages: List<Locale>,
        context: Context,
    )
}
