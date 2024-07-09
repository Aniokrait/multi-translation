package io.github.aniokrait.multitranslation.datasource

import com.google.mlkit.common.model.RemoteModelManager
import com.google.mlkit.nl.translate.TranslateRemoteModel
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test
import java.util.Locale

class LanguageModelDatasourceTest {

    @Test
    fun testPutDownloadedTrueWhenDownloadModelSucceeded() = runTest {

        val datasource = LanguageModelDatasource()

        val targetLanguages = listOf(Locale.ENGLISH, Locale.GERMAN)
        datasource.downloadModel(
            targetLanguages = targetLanguages,
        )

        val downloadedModelsTask =
            RemoteModelManager.getInstance().getDownloadedModels(TranslateRemoteModel::class.java)
        val downloadedModels = downloadedModelsTask.await()

        val isEnglishDownloaded = downloadedModels.any { it.language == Locale.ENGLISH.language }
        val isGermanDownloaded = downloadedModels.any { it.language == Locale.GERMAN.language }

        assertEquals(true, isEnglishDownloaded)
        assertEquals(true, isGermanDownloaded)
    }
}
