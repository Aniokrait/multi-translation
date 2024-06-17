package io.github.aniokrait.multitranslation.datasource

import io.github.aniokrait.multitranslation.repository.LanguageModelRepository
import io.github.aniokrait.multitranslation.ui.stateholder.DownloadedState
import kotlinx.coroutines.flow.Flow

class LanguageModelDatasource : LanguageModelRepository {
    override fun getDownloadedInfo(): Flow<List<DownloadedState>> {
        TODO("Not yet implemented")
    }


    override fun getSimpleState(): Flow<Int> {
        TODO("Not yet implemented")
    }

    override fun updateSimpleState() {
        TODO("Not yet implemented")
    }
}
