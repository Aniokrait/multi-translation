package io.github.aniokrait.multitranslation.datasource.fake

import androidx.compose.runtime.mutableStateOf
import io.github.aniokrait.multitranslation.repository.LanguageModelRepository
import io.github.aniokrait.multitranslation.ui.stateholder.LanguageDownloadState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.util.Locale

class FakeLanguageModelDatasource : LanguageModelRepository {
    // Todo: replace to datastore in the real object
    private var list: List<LanguageDownloadState.EachLanguageState> = listOf(
        LanguageDownloadState.EachLanguageState(
            locale = Locale.JAPANESE,
            checked = mutableStateOf(false),
            downloaded = mutableStateOf(false)
        )
    )

    override fun getDownloadedInfo(): Flow<List<LanguageDownloadState.EachLanguageState>> = flow {
        emit(list)

    }

    override fun checkLanguage(locale: Locale) {
        // todo: replace to change datastore value in the real object
        list.find { it.locale == locale }?.checked?.value = true
    }

    var count = 1
    override fun getSimpleState(): Flow<Int> = flow {
        emit(count)
    }

    override fun updateSimpleState() {
        count++
    }

}
