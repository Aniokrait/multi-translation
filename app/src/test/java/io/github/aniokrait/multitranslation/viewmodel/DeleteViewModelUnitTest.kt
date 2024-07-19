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
import org.junit.Before
import org.junit.Test

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
    fun testInitUiState() = runTest {
        val result = deleteModelViewModel.uiState.first()
        assertEquals(2, result.languagesState.size)
    }
}
