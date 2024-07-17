package io.github.aniokrait.multitranslation.viewmodel

import androidx.compose.runtime.mutableStateOf
import io.github.aniokrait.multitranslation.datasource.fake.FakeLanguageModelDatasource
import io.mockk.Called
import io.mockk.mockk
import io.mockk.spyk
import io.mockk.verify
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
import org.junit.Assert.assertFalse
import org.junit.Before
import org.junit.Test
import java.util.Locale

@OptIn(ExperimentalCoroutinesApi::class)
class TranslationModelDownloadViewModelTest {
    private lateinit var translationModelDownloadViewModel: TranslationModelDownloadViewModel
    private lateinit var fakeRepository: FakeLanguageModelDatasource
    private lateinit var dispatcher: CoroutineDispatcher

    @Before
    fun setUp() {
        dispatcher = UnconfinedTestDispatcher()
        Dispatchers.setMain(dispatcher)
        fakeRepository = FakeLanguageModelDatasource()
        translationModelDownloadViewModel =
            TranslationModelDownloadViewModel(repository = fakeRepository)
    }

    @Test
    fun testGetStateFlow() = runTest(dispatcher) {
        val result = translationModelDownloadViewModel.downloadState.first()
        advanceUntilIdle()
        assertEquals(1, result.languagesState.size)
        assertEquals(Locale.JAPANESE, result.languagesState.first().locale)
    }

    @Test
    fun testUpdateChecked() = runTest(dispatcher) {
        val before = translationModelDownloadViewModel
            .downloadState
            .first()
            .languagesState
            .first { it.locale == Locale.JAPANESE }
            .checked.value
        assertFalse(before)

        translationModelDownloadViewModel.onCheckClicked(Locale.JAPANESE)

        val result = translationModelDownloadViewModel
            .downloadState
            .first()
            .languagesState
            .first { it.locale == Locale.JAPANESE }
            .checked.value
        assertTrue(result)
    }

    @Test
    fun testNavigateToTranslationAfterDownloadSuccess() = runTest(dispatcher) {
        // Assuming german was checked.
        translationModelDownloadViewModel.onCheckClicked(locale = Locale.GERMAN)

        val mockLambda = spyk<() -> Unit>()
        translationModelDownloadViewModel.onDownloadClicked(
            navigateToTranslation = mockLambda,
            errorMessageTemplate = "",
            snackBarMessageState = mutableStateOf("")
        )

        verify { mockLambda.invoke() }
    }

    @Test
    fun testDoNothingWhenDownloadAllFailed() = runTest(dispatcher) {
        // Assuming japanese and german was checked.
        translationModelDownloadViewModel.onCheckClicked(locale = Locale.JAPANESE)
        translationModelDownloadViewModel.onCheckClicked(locale = Locale.GERMAN)
        fakeRepository.failDownloadModel = true

        val mockLambda = mockk<() -> Unit>()
        translationModelDownloadViewModel.onDownloadClicked(
            navigateToTranslation = mockLambda,
            errorMessageTemplate = "",
            snackBarMessageState = mutableStateOf("")
        )

        verify { mockLambda wasNot Called }
    }

    @Test
    fun testShowSnackBarIfCheckedCountDiffersFailedCount() {
        // Assuming japanese, chinese and german was checked.
        translationModelDownloadViewModel.onCheckClicked(locale = Locale.JAPANESE)
        translationModelDownloadViewModel.onCheckClicked(locale = Locale.CHINESE)
        translationModelDownloadViewModel.onCheckClicked(locale = Locale.GERMAN)
        fakeRepository.failDownloadModel = true

        val mockLambda = spyk<() -> Unit>()
        val snackBarMessageState = mutableStateOf("")
        translationModelDownloadViewModel.onDownloadClicked(
            navigateToTranslation = mockLambda,
            errorMessageTemplate = "test",
            snackBarMessageState = snackBarMessageState
        )

        assertEquals(
            "test${System.lineSeparator()}・日本語${System.lineSeparator()}・ドイツ語",
            snackBarMessageState.value
        )
    }
}
