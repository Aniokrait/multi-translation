package io.github.aniokrait.multitranslation.datasource

import android.app.Application
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.test.core.app.ApplicationProvider
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test
import java.util.Locale

class LanguageModelDatasourceTest {

    @Test
    fun testPutDownloadedTrueWhenDownloadModelSucceeded() = runTest {

        val context = ApplicationProvider.getApplicationContext<Application>()
        val datasource = LanguageModelDatasource(context = context)

        val targetLanguages = listOf(Locale.ENGLISH, Locale.GERMAN)
        datasource.downloadModel(
            targetLanguages = targetLanguages,
            context = context,
        )

        val isEnglishDownloaded = context.dataStore.data.map {
            it[booleanPreferencesKey(Locale.ENGLISH.language)]
        }.first()
        val isGermanDownloaded = context.dataStore.data.map {
            it[booleanPreferencesKey(Locale.ENGLISH.language)]
        }.first()

        assertEquals(true, isEnglishDownloaded)
        assertEquals(true, isGermanDownloaded)
    }
}
