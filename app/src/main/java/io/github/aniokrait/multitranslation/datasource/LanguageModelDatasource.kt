package io.github.aniokrait.multitranslation.datasource

import android.content.Context
import androidx.compose.runtime.mutableStateOf
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import io.github.aniokrait.multitranslation.core.LanguageNameResolver
import io.github.aniokrait.multitranslation.repository.LanguageModelRepository
import io.github.aniokrait.multitranslation.ui.stateholder.DownloadedState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

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
}
