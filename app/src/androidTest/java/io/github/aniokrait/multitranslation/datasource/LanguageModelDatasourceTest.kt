package io.github.aniokrait.multitranslation.datasource

import com.google.mlkit.common.model.RemoteModelManager
import com.google.mlkit.nl.translate.TranslateRemoteModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import java.util.Locale

class LanguageModelDatasourceTest {
    @Test
    fun Get_downloaded_models_state() = runTest {
        val datasource = LanguageModelDatasource()
        downloadModelBeforeTesting(datasource = datasource)

        val result = datasource.getDownloadedState().first()
        assertEquals(59, result.size)

        val downloadedModels = result.filter { it.downloaded.value }
        assertEquals(2, downloadedModels.size)
    }

    @Test
    fun Get_downloaded_remote_models() = runTest {
        val datasource = LanguageModelDatasource()
        downloadModelBeforeTesting(datasource = datasource)

        val result = datasource.getDownloadedRemoteModels()
        assertEquals(2, result.size)
        assertTrue(result.any { it.language == Locale.GERMAN.language })
    }

    @Test
    fun Download_translation_models() = runTest {
        val datasource = LanguageModelDatasource()

        val targetLanguages = listOf(Locale.ENGLISH, Locale.GERMAN)
        datasource.downloadModel(
            targetLanguages = targetLanguages,
            allowNoWifi = false,
            successDownloadedCount = MutableStateFlow(0),
        )

        val downloadedModelsTask =
            RemoteModelManager.getInstance()
                .getDownloadedModels(TranslateRemoteModel::class.java)
        val downloadedModels = downloadedModelsTask.await()

        val isEnglishDownloaded =
            downloadedModels.any { it.language == Locale.ENGLISH.language }
        val isGermanDownloaded = downloadedModels.any { it.language == Locale.GERMAN.language }

        assertEquals(true, isEnglishDownloaded)
        assertEquals(true, isGermanDownloaded)
    }

    @Test
    fun Delete_downloaded_tlanslation_models() = runTest {
        val datasource = LanguageModelDatasource()

        // Assert that there is no model before testing.
        val modelsBeforeTesting = datasource.getDownloadedRemoteModels().omitExtraModel()
        assertEquals(0, modelsBeforeTesting.size)

        // Assert models count is incremented after downloading.
        downloadModelBeforeTesting(datasource = datasource)
        val modelsAfterDownloading = datasource.getDownloadedRemoteModels().omitExtraModel()
        assertEquals(1, modelsAfterDownloading.size)

        datasource.deleteModel(listOf(Locale.GERMAN))

        val modelsAfterDeleting = datasource.getDownloadedRemoteModels().omitExtraModel()
        assertEquals(0, modelsAfterDeleting.size)
    }

    private fun Set<TranslateRemoteModel>.omitExtraModel(): Set<TranslateRemoteModel> {
        return this.filter { it.language != Locale.ENGLISH.language && it.language != Locale.JAPANESE.language }
            .toSet()
    }

    private fun downloadModelBeforeTesting(datasource: LanguageModelDatasource) = runTest {
        // Assert models count is incremented after downloading.
        datasource.downloadModel(
            targetLanguages = listOf(Locale.GERMAN),
            allowNoWifi = false,
            successDownloadedCount = MutableStateFlow(0),
        )
    }
}
