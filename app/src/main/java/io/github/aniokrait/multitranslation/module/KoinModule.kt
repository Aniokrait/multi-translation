package io.github.aniokrait.multitranslation.module

import io.github.aniokrait.multitranslation.datasource.LanguageModelDatasource
import io.github.aniokrait.multitranslation.repository.LanguageModelRepository
import io.github.aniokrait.multitranslation.viewmodel.TranslationModelDownloadViewModel
import io.github.aniokrait.multitranslation.viewmodel.TranslationViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    single<LanguageModelRepository> { LanguageModelDatasource(context = androidContext()) }
    viewModel { TranslationModelDownloadViewModel(repository = get()) }
    viewModel { TranslationViewModel(context = androidContext()) }
}
