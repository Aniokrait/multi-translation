package io.github.aniokrait.multitranslation.datasource

import android.content.Context
import androidx.compose.runtime.mutableStateOf
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import com.google.mlkit.common.model.DownloadConditions
import com.google.mlkit.nl.translate.TranslateLanguage
import com.google.mlkit.nl.translate.Translation
import com.google.mlkit.nl.translate.Translator
import com.google.mlkit.nl.translate.TranslatorOptions
import io.github.aniokrait.multitranslation.core.LanguageNameResolver
import io.github.aniokrait.multitranslation.repository.LanguageModelRepository
import io.github.aniokrait.multitranslation.ui.stateholder.DownloadedState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.util.Locale

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class LanguageModelDatasource(
    private val context: Context,
) : LanguageModelRepository {
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
    ) {
        val conditions = DownloadConditions.Builder()
            .requireWifi()
            .build()

        val tasks: MutableList<Job> = mutableListOf()
        targetLanguages.forEach { locale ->
            val options = TranslatorOptions.Builder()
                .setSourceLanguage(TranslateLanguage.JAPANESE)
                .setTargetLanguage(locale.language)
                .build()
            val translator: Translator = Translation.getClient(options)

            tasks.add(CoroutineScope(Dispatchers.IO).launch {
                try {
                    translator.downloadModelIfNeeded(conditions).await()
                    context.dataStore.edit {
                        it[booleanPreferencesKey(locale.language)] = true
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }

            })
        }

        tasks.joinAll()
    }
}
