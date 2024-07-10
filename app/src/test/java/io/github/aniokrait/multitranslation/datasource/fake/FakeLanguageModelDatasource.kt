package io.github.aniokrait.multitranslation.datasource.fake

import androidx.compose.runtime.mutableStateOf
import io.github.aniokrait.multitranslation.repository.LanguageModelRepository
import io.github.aniokrait.multitranslation.ui.stateholder.DownloadedState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.util.Locale

class FakeLanguageModelDatasource : LanguageModelRepository {
    // Todo: replace to datastore in the real object
    private var list: List<DownloadedState> = listOf(
        DownloadedState(
            locale = Locale.JAPANESE,
            downloaded = mutableStateOf(false)
        )
    )

    override fun getDownloadedInfo(): Flow<List<DownloadedState>> = flow {
        emit(list)

    }

    override suspend fun downloadModel(
        targetLanguages: List<Locale>,
    ): List<Locale> {
        TODO("Not yet implemented")
    }

}
