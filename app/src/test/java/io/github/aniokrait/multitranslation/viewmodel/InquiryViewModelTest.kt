package io.github.aniokrait.multitranslation.viewmodel

import io.github.aniokrait.multitranslation.datasource.fake.FakeSlackBotInquirer
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class InquiryViewModelTest {
    private lateinit var fakeRepository: FakeSlackBotInquirer
    private lateinit var dispatcher: CoroutineDispatcher

    @Before
    fun setup() {
        fakeRepository = FakeSlackBotInquirer()

        dispatcher = StandardTestDispatcher()
        Dispatchers.setMain(dispatcher)
    }

    @Test
    fun testSendInquiry() = runTest {
        val viewModel = InquiryViewModel(repository = fakeRepository)

        viewModel.sendInquiry("test content")
        advanceUntilIdle()

        assertEquals(InquiryUiState.SentSuccess, viewModel.uiState.value)
    }
}
