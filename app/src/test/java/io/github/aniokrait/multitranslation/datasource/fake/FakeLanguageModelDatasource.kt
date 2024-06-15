package io.github.aniokrait.multitranslation.datasource.fake

import androidx.compose.runtime.mutableStateOf
import io.github.aniokrait.multitranslation.repository.LanguageModelRepository
import io.github.aniokrait.multitranslation.ui.stateholder.LanguageDownloadState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.util.Locale

class FakeLanguageModelDatasource : LanguageModelRepository {
    override fun getDownloadedInfo(): Flow<List<LanguageDownloadState.EachLanguageState>> = flow {
        val list = listOf(
            LanguageDownloadState.EachLanguageState(
                locale = Locale.JAPANESE,
                checked = mutableStateOf(false),
                downloaded = mutableStateOf(false)
            )
        )
        emit(list)

    }

}
