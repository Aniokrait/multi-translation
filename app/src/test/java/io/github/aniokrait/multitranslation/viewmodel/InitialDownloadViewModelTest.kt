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
    lateinit var initialDownloadViewModel: InitialDownloadViewModel
    lateinit var fakeRepository: FakeLanguageModelDatasource
    lateinit var dispatcher: CoroutineDispatcher

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
        assertEquals(1, result.size)
        assertEquals(Locale.JAPANESE, result.first().locale)
    }

    @Test
    fun testUpdateChecked() = runTest(dispatcher) {
        initialDownloadViewModel.onCheckClicked(Locale.JAPANESE)
        
        val result = initialDownloadViewModel.downloadState.first()
        assertEquals(1, result.size)
        assertTrue(result.first().checked.value)
    }

    @Test
    fun testInitialSimpleState() = runTest(dispatcher) {
        val result = initialDownloadViewModel.simpleState.first()
        assertEquals(1, result)
    }

    @Test
    fun testUpdateSimpleState() = runTest(dispatcher) {
        initialDownloadViewModel.updateSimpleState()
        val result = initialDownloadViewModel.simpleState.first()
        assertEquals(2, result)
    }
}
