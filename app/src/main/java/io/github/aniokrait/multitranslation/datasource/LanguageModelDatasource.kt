package io.github.aniokrait.multitranslation.datasource

import io.github.aniokrait.multitranslation.repository.LanguageModelRepository
import io.github.aniokrait.multitranslation.ui.stateholder.LanguageDownloadState
import kotlinx.coroutines.flow.Flow
import java.util.Locale

class LanguageModelDatasource : LanguageModelRepository {
    override fun getDownloadedInfo(): Flow<List<LanguageDownloadState.EachLanguageState>> {
        TODO("Not yet implemented")
    }

    override fun checkLanguage(locale: Locale) {
        TODO("Not yet implemented")
    }

    override fun getSimpleState(): Flow<Int> {
        TODO("Not yet implemented")
    }

    override fun updateSimpleState() {
        TODO("Not yet implemented")
    }
}
