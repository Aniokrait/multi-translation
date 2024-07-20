package io.github.aniokrait.multitranslation.viewmodel

import io.github.aniokrait.multitranslation.datasource.fake.FakeLanguageModelDatasource
import io.github.aniokrait.multitranslation.repository.LanguageModelRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import java.util.Locale

@OptIn(ExperimentalCoroutinesApi::class)
class DeleteViewModelUnitTest {
    private lateinit var deleteModelViewModel: DeleteModelViewModel
    private lateinit var repository: LanguageModelRepository
    private lateinit var dispatcher: CoroutineDispatcher

    @Before
    fun setUp() {
        dispatcher = UnconfinedTestDispatcher()
        Dispatchers.setMain(dispatcher)
        repository = FakeLanguageModelDatasource()

        deleteModelViewModel =
            DeleteModelViewModel(repository = repository)
    }

    @Test
    fun deleteModels() = runTest {
        // Assert initial state before deleting.
        repository.downloadModel(listOf(Locale.GERMAN, Locale.CHINESE, Locale.ENGLISH))
        val downloadedModelsBeforeDelete = deleteModelViewModel.uiState.first()
        assertEquals(4, downloadedModelsBeforeDelete.languagesState.size)

        deleteModelViewModel.onDeleteClicked(listOf(Locale.GERMAN, Locale.CHINESE))

        val result = deleteModelViewModel.uiState.first()
        assertEquals(2, result.languagesState.size)
    }

    @Test
    fun testUpdateChecked() = runTest(dispatcher) {
        val before = deleteModelViewModel.uiState
            .first()
            .languagesState
            .first { it.locale == Locale.JAPANESE }
            .checked.value
        assertFalse(before)

        deleteModelViewModel.onCheckClicked(Locale.JAPANESE)
        val result = deleteModelViewModel.uiState.first()
            .languagesState
            .first { it.locale == Locale.JAPANESE }
            .checked.value
        assertTrue(result)
    }
}
