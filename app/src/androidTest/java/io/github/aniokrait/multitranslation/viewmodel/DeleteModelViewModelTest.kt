package io.github.aniokrait.multitranslation.viewmodel

import com.google.mlkit.common.model.RemoteModelManager
import com.google.mlkit.nl.translate.TranslateRemoteModel
import io.github.aniokrait.multitranslation.datasource.LanguageModelDatasource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import java.util.Locale

@OptIn(ExperimentalCoroutinesApi::class)
class DeleteModelViewModelTest {
    private lateinit var deleteModelViewModel: DeleteModelViewModel
    private lateinit var repository: LanguageModelDatasource
    private lateinit var dispatcher: CoroutineDispatcher

    @Before
    fun setUp() {
        dispatcher = UnconfinedTestDispatcher()
        repository = LanguageModelDatasource(ioDispatcher = dispatcher)

        runTest {
            assumeModelHasBeenDownloaded()
        }

        deleteModelViewModel =
            DeleteModelViewModel(repository = repository)
    }

    @Test
    fun `テスト中でダウンロードして、結果を取得できることを確認`() = runTest {
        val b = RemoteModelManager.getInstance()
            .getDownloadedModels(TranslateRemoteModel::class.java)
        val c = b.await()
        assertEquals(4, c.size)
    }

    private suspend fun assumeModelHasBeenDownloaded() {
        repository.downloadModel(
            listOf(
                Locale.GERMAN,
                Locale.CHINESE,
            )
        )
    }
}
