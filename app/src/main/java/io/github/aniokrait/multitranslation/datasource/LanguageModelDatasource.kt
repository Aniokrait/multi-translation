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
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.asDeferred
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.util.Locale
import java.util.concurrent.TimeoutException

class LanguageModelDatasource(
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO,
) : LanguageModelRepository {
    companion object {
        const val TAG = "LanguageModelDatasource"
    }

    /**
     * Get downloaded models info and emit state.
     */
    override fun getDownloadedInfo(): Flow<List<DownloadedState>> = flow {
        val downloadedModels = getDownloadedModels()

        emit(LanguageNameResolver.getAllLanguagesLabel().map { locale ->
            val isDownloaded = downloadedModels.any { it.language == locale.language }
            DownloadedState(
                locale = locale,
                downloaded = mutableStateOf(isDownloaded)
            )
        })
    }

    /**
     * Get downloaded models.
     */
    override suspend fun getDownloadedModels(): Set<TranslateRemoteModel> {
        return withContext(ioDispatcher) {
            val downloadedModelsTask =
                RemoteModelManager.getInstance()
                    .getDownloadedModels(TranslateRemoteModel::class.java)
            return@withContext downloadedModelsTask.await()
        }
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
        targetLanguages
            // FIXME: Hide Japanese till implementing changing source language.
            .filter { it != Locale.JAPANESE }
            .forEach { locale ->
                val options = TranslatorOptions.Builder()
                    .setSourceLanguage(TranslateLanguage.JAPANESE)
                    .setTargetLanguage(locale.language)
                    .build()
                val translator: Translator = Translation.getClient(options)

                try {
                    withContext(Dispatchers.IO) {
                        val deferred = translator.downloadModelIfNeeded(conditions).asDeferred()

                        val timeoutJob = launch {
                            // If download doesn't complete in 10 seconds, throw an exception.
                            delay(10000)
                            // FIXME: Even if cancels here, download will happen as soon as device is connected to the internet.
                            deferred.cancel()
                            throw TimeoutException("Download timed out for ${locale.language}.")
                        }

                        deferred.await()
                        timeoutJob.cancel()
                    }
                } catch (e: TimeoutException) {
                    Log.w(TAG, "${e.message}")
                    failedModels.add(locale)
                }
            }

        return failedModels.toList()
    }
}
