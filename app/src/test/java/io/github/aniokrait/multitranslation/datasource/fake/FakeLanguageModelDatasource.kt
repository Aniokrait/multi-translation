package io.github.aniokrait.multitranslation.datasource.fake

import androidx.compose.runtime.mutableStateOf
import com.google.mlkit.nl.translate.TranslateRemoteModel
import io.github.aniokrait.multitranslation.repository.LanguageModelRepository
import io.github.aniokrait.multitranslation.ui.stateholder.DownloadedState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.util.Locale

class FakeLanguageModelDatasource : LanguageModelRepository {
    private var set: MutableSet<DownloadedState> = mutableSetOf(
        DownloadedState(
            locale = Locale.JAPANESE,
            downloaded = mutableStateOf(false)
        )
    )

    override fun getDownloadedInfo(): Flow<List<DownloadedState>> = flow {
        emit(set.toList())

    }

    override suspend fun getDownloadedModels(): Set<TranslateRemoteModel> {
        return set.map { TranslateRemoteModel.Builder(it.locale.toLanguageTag()).build() }.toSet()
    }

    var failDownloadModel = false
    override suspend fun downloadModel(
        targetLanguages: List<Locale>,
        allowNoWifi: Boolean,
    ): List<Locale> {
        set.addAll(targetLanguages.map { DownloadedState(it, mutableStateOf(true)) })

        return if (failDownloadModel) {
            listOf(Locale.JAPANESE, Locale.GERMAN)
        } else {
            emptyList()
        }
    }

    override suspend fun deleteModel(targetLanguages: List<Locale>) {
        val targetModels = targetLanguages.map {
            DownloadedState(
                locale = Locale.forLanguageTag(it.language),
                downloaded = mutableStateOf(true)
            )
        }

        targetModels.forEach {
            set.removeIf { downloadedState -> downloadedState.locale == it.locale }
        }
    }

}
