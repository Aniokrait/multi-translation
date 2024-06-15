package io.github.aniokrait.multitranslation.viewmodel

import io.github.aniokrait.multitranslation.datasource.fake.FakeLanguageModelDatasource
import junit.framework.TestCase.assertEquals
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

class InitialDownloadViewModelTest {
    lateinit var initialDownloadViewModel: InitialDownloadViewModel
    lateinit var fakeRepository: FakeLanguageModelDatasource
    lateinit var dispatcher: CoroutineDispatcher

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setUp() {
        dispatcher = UnconfinedTestDispatcher()
        Dispatchers.setMain(dispatcher)
        fakeRepository = FakeLanguageModelDatasource()
        initialDownloadViewModel =
            InitialDownloadViewModel(repository = fakeRepository)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun testStateFlow() = runTest(dispatcher) {
        val result = initialDownloadViewModel.downloadState.first()
        advanceUntilIdle()
        assertEquals(1, result.size)
        assertEquals(Locale.JAPANESE, result.first().locale)
    }
}
