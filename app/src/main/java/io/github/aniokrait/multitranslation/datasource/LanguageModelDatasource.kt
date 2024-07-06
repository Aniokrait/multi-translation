package io.github.aniokrait.multitranslation.datasource

import android.content.Context
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import com.google.mlkit.common.model.DownloadConditions
import com.google.mlkit.nl.translate.TranslateLanguage
import com.google.mlkit.nl.translate.Translation
import com.google.mlkit.nl.translate.Translator
import com.google.mlkit.nl.translate.TranslatorOptions
import io.github.aniokrait.multitranslation.core.LanguageNameResolver
import io.github.aniokrait.multitranslation.extension.dataStore
import io.github.aniokrait.multitranslation.repository.LanguageModelRepository
import io.github.aniokrait.multitranslation.ui.stateholder.DownloadedState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.util.Locale
import java.util.concurrent.TimeoutException

class LanguageModelDatasource(
    private val context: Context,
) : LanguageModelRepository {
    companion object {
        const val TAG = "LanguageModelDatasource"
    }

    override fun getDownloadedInfo(): Flow<List<DownloadedState>> = flow {
        val isDatastoreEmpty: Boolean = context.dataStore.data
            .map { preferences ->
                return@map preferences.asMap().isEmpty()
            }.first()

        // datastoreが空の場合はallLanguageに対して、downloadedをfalseにする。
        if (isDatastoreEmpty) {
            emit(LanguageNameResolver.getAllLanguagesLabel().map { locale ->
                DownloadedState(
                    locale = locale,
                    downloaded = mutableStateOf(false)
                )
            })
        }

        // allLanguagesからロケールをすべて取得する。
        // ループを回し、ひとつひとつdatastoreから該当する項目を取得する。
        emit(LanguageNameResolver.getAllLanguagesLabel().map { locale ->
            val isDownloaded = context.dataStore.data.map { preferences ->
                preferences[booleanPreferencesKey(locale.language)] ?: false
            }.first()

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
     * @param context Context is needed to store download result to DataStore.
     */
    override suspend fun downloadModel(
        targetLanguages: List<Locale>,
        context: Context,
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

                    context.dataStore.edit {
                        it[booleanPreferencesKey(locale.language)] = true
                    }
                }
            } catch (e: TimeoutException) {
                Log.w(TAG, "Download timed out for ${locale.language}.")
                failedModels.add(locale)
            }
        }

        return failedModels.toList()
    }
}
