package io.github.aniokrait.multitranslation.datasource

import io.github.aniokrait.multitranslation.repository.LanguageModelRepository
import io.github.aniokrait.multitranslation.ui.stateholder.LanguageDownloadState
import kotlinx.coroutines.flow.Flow

class LanguageModelDatasource : LanguageModelRepository {
    override fun getDownloadedInfo(): Flow<List<LanguageDownloadState.EachLanguageState>> {
        TODO("Not yet implemented")
    }
}
