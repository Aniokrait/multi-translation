package io.github.aniokrait.multitranslation.viewmodel

import io.github.aniokrait.multitranslation.datasource.fake.FakeLanguageModelDatasource
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Test
import java.util.Locale

@OptIn(ExperimentalCoroutinesApi::class)
class InitialDownloadViewModelTest {
    private lateinit var initialDownloadViewModel: InitialDownloadViewModel
    private lateinit var fakeRepository: FakeLanguageModelDatasource
    private lateinit var dispatcher: CoroutineDispatcher

    @Before
    fun setUp() {
        dispatcher = UnconfinedTestDispatcher()
        Dispatchers.setMain(dispatcher)
        fakeRepository = FakeLanguageModelDatasource()
        initialDownloadViewModel =
            InitialDownloadViewModel(repository = fakeRepository)
    }

    @Test
    fun testGetStateFlow() = runTest(dispatcher) {
        val result = initialDownloadViewModel.downloadState.first()
        advanceUntilIdle()
        assertEquals(1, result.languagesState.size)
        assertEquals(Locale.JAPANESE, result.languagesState.first().locale)
    }

    @Test
    fun testUpdateChecked() = runTest(dispatcher) {
        initialDownloadViewModel.onCheckClicked(Locale.JAPANESE)

        val result = initialDownloadViewModel.downloadState.first()
        assertEquals(1, result.languagesState.size)
        assertTrue(result.languagesState.first().checked.value)
    }

    @Test
    fun testFold() {
        val strList = listOf("Japanese", "English")
        val result = strList.fold("Initial") { acc, s -> "$acc\n$s" }
        assertEquals("Initial\nJapanese\nEnglish", result)
    }
}
