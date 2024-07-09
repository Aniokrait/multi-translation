package io.github.aniokrait.multitranslation.datasource

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import com.google.mlkit.common.model.DownloadConditions
import com.google.mlkit.common.model.RemoteModelManager
import com.google.mlkit.nl.translate.TranslateLanguage
import com.google.mlkit.nl.translate.TranslateRemoteModel
import com.google.mlkit.nl.translate.Translation
import com.google.mlkit.nl.translate.Translator
import com.google.mlkit.nl.translate.TranslatorOptions
import io.github.aniokrait.multitranslation.core.LanguageNameResolver
import io.github.aniokrait.multitranslation.repository.LanguageModelRepository
import io.github.aniokrait.multitranslation.ui.stateholder.DownloadedState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.util.Locale
import java.util.concurrent.TimeoutException

class LanguageModelDatasource : LanguageModelRepository {
    companion object {
        const val TAG = "LanguageModelDatasource"
    }

    /**
     * Get downloaded models info and emit state.
     */
    override fun getDownloadedInfo(): Flow<List<DownloadedState>> = flow {
        val downloadedModelsTask =
            RemoteModelManager.getInstance().getDownloadedModels(TranslateRemoteModel::class.java)
        val downloadedModels = downloadedModelsTask.await()

        emit(LanguageNameResolver.getAllLanguagesLabel().map { locale ->
            val isDownloaded = downloadedModels.any { it.language == locale.language }
            DownloadedState(
                locale = locale,
                downloaded = mutableStateOf(isDownloaded)
            )
        })
    }


    override fun getSimpleState(): Flow<Int> {
        TODO("Not yet implemented")
    }

    override fun updateSimpleState() {
        TODO("Not yet implemented")
    }

    /**
     * Download language translation models.
     * @param targetLanguages Checked languages on selection screen.
     */
    override suspend fun downloadModel(
        targetLanguages: List<Locale>,
    ): List<Locale> {
        val conditions = DownloadConditions.Builder()
            .requireWifi()
            .build()

        val failedModels = mutableListOf<Locale>()
        targetLanguages.forEach { locale ->
            val options = TranslatorOptions.Builder()
                .setSourceLanguage(TranslateLanguage.JAPANESE)
                .setTargetLanguage(locale.language)
                .build()
            val translator: Translator = Translation.getClient(options)

            try {
                withContext(Dispatchers.IO) {
                    val timeoutJob = launch {
                        // If download doesn't complete in 10 seconds, throw an exception.
                        delay(10000)
                        throw TimeoutException("Download timed out")
                    }
                    translator.downloadModelIfNeeded(conditions).await()
                    timeoutJob.cancel()
                }
            } catch (e: TimeoutException) {
                Log.w(TAG, "Download timed out for ${locale.language}.")
                failedModels.add(locale)
            }
        }

        return failedModels.toList()
    }
}
