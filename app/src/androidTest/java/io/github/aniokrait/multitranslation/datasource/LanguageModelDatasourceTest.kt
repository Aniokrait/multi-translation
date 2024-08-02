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
            allowNoWifi = false,
        )

        val downloadedModelsTask =
            RemoteModelManager.getInstance().getDownloadedModels(TranslateRemoteModel::class.java)
        val downloadedModels = downloadedModelsTask.await()

        val isEnglishDownloaded = downloadedModels.any { it.language == Locale.ENGLISH.language }
        val isGermanDownloaded = downloadedModels.any { it.language == Locale.GERMAN.language }

        assertEquals(true, isEnglishDownloaded)
        assertEquals(true, isGermanDownloaded)
    }

    @Test
    fun testDeleteLanguageModel() = runTest {
        val datasource = LanguageModelDatasource()

        // Assert that there is no model before testing.
        val modelsBeforeTesting = datasource.getDownloadedModels().omitExtraModel()
        assertEquals(0, modelsBeforeTesting.size)

        // Assert models count is incremented after downloading.
        datasource.downloadModel(
            targetLanguages = listOf(Locale.GERMAN),
            allowNoWifi = false,
        )
        val modelsAfterDownloading = datasource.getDownloadedModels().omitExtraModel()
        assertEquals(1, modelsAfterDownloading.size)

        datasource.deleteModel(listOf(Locale.GERMAN))

        val modelsAfterDeleting = datasource.getDownloadedModels().omitExtraModel()
        assertEquals(0, modelsAfterDeleting.size)
    }

    private fun Set<TranslateRemoteModel>.omitExtraModel(): Set<TranslateRemoteModel> {
        return this.filter { it.language != Locale.ENGLISH.language && it.language != Locale.JAPANESE.language }
            .toSet()
    }
}
