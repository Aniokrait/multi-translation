package io.github.aniokrait.multitranslation.datasource

import androidx.compose.runtime.mutableStateOf
import com.google.mlkit.common.MlKitException
import com.google.mlkit.common.model.DownloadConditions
import com.google.mlkit.common.model.RemoteModelManager
import com.google.mlkit.nl.translate.TranslateLanguage
import com.google.mlkit.nl.translate.TranslateRemoteModel
import com.google.mlkit.nl.translate.Translation
import com.google.mlkit.nl.translate.Translator
import com.google.mlkit.nl.translate.TranslatorOptions
import io.github.aniokrait.multitranslation.core.LanguageNameResolver
import io.github.aniokrait.multitranslation.repository.DownloadResult
import io.github.aniokrait.multitranslation.repository.LanguageModelRepository
import io.github.aniokrait.multitranslation.ui.stateholder.DownloadedState
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.tasks.asDeferred
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.util.Locale
import java.util.concurrent.TimeoutException

class LanguageModelDatasource(
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO,
    private val mainDispatcher: CoroutineDispatcher = Dispatchers.Main,
) : LanguageModelRepository {
    /**
     * Get downloaded models info and emit state.
     */
    override fun getDownloadedInfo(): Flow<List<DownloadedState>> =
        flow {
            val downloadedModels = getDownloadedModels()

            emit(
                LanguageNameResolver.getAllLanguagesLabel().map { locale ->
                    val isDownloaded =
                        downloadedModels.any { it.language == locale.toLanguageTag() }
                    DownloadedState(
                        locale = locale,
                        downloaded = mutableStateOf(isDownloaded),
                    )
                },
            )
        }

    /**
     * Get downloaded models.
     */
    override suspend fun getDownloadedModels(): Set<TranslateRemoteModel> {
        return withContext(ioDispatcher) {
            val downloadedModelsTask =
                RemoteModelManager.getInstance()
                    .getDownloadedModels(TranslateRemoteModel::class.java)
            return@withContext downloadedModelsTask.await()
        }
    }

    /**
     * Download language translation models.
     * @param targetLanguages Checked languages on selection screen.
     * @param allowNoWifi Allow download even if the device is not connected to the internet.
     * @param successDownloadedCount The number of successfully downloaded models.
     */
    override suspend fun downloadModel(
        targetLanguages: List<Locale>,
        allowNoWifi: Boolean,
        successDownloadedCount: MutableStateFlow<Int>,
    ): DownloadResult {
        val conditionsBuilder = DownloadConditions.Builder()
        if (!allowNoWifi) {
            conditionsBuilder.requireWifi()
        }
        val conditions = conditionsBuilder.build()

        val notEnoughSpace = MutableStateFlow(false)
        val mutex = Mutex()
        val tasks: MutableList<Deferred<Void>> = mutableListOf()
        val failedModels = mutableListOf<Locale>()
        targetLanguages
            .filter { it != Locale.getDefault() }
            .forEach { locale ->
                val options =
                    TranslatorOptions.Builder()
                        .setSourceLanguage(
                            TranslateLanguage.ENGLISH,
                        ) // All languages are translated via English according to the firebase specification.
                        .setTargetLanguage(locale.toLanguageTag())
                        .build()
                val translator: Translator = Translation.getClient(options)

                try {
                    withContext(ioDispatcher) {
                        val deferred =
                            translator.downloadModelIfNeeded(conditions)
                                .addOnFailureListener {
                                    onDownloadFailure(
                                        ex = it,
                                        notEnoughSpace = notEnoughSpace,
                                    )
                                }
                                .addOnSuccessListener {
                                    onDownloadSuccess(
                                        mutex = mutex,
                                        successDownloadedCount = successDownloadedCount,
                                    )
                                }
                                .asDeferred()

                        // TODO Fix show downloaded models in ui asynchronously.
//                        val timeoutJob = launch {
//                            // If download doesn't complete in 10 seconds, throw an exception.
//                            delay(10000)
//                            // FIXME: Even if cancels here, download will happen as soon as device is connected to the internet.
//                            deferred.cancel()
//                            throw TimeoutException("Download timed out for ${locale.language}.")
//                        }

                        tasks.add(deferred)
//                        deferred.await()
//                        timeoutJob.cancel()
                    }
                } catch (e: TimeoutException) {
                    Timber.w(e.message)
                    failedModels.add(locale)
                } finally {
                    translator.close()
                }

                // TODO: Fix issue not enough space.
                if (notEnoughSpace.value) {
                    Timber.w("Not enough space.")
                    return DownloadResult.NotEnoughSpace(failedModels)
                }
            }

        val jobsWithoutUserLocale = tasks.map {
            CoroutineScope(ioDispatcher).launch {
                it.await()
            }
        }

        val allJobs = jobsWithoutUserLocale.toMutableList()
        allJobs.add(
            CoroutineScope(ioDispatcher).launch {
                downloadUserLocaleModel(
                    conditions = conditions,
                    notEnoughSpace = notEnoughSpace,
                    mutex = mutex,
                ).await()
            }
        )
        allJobs.joinAll()

        return if (failedModels.isEmpty()) {
            DownloadResult.Success
        } else {
            DownloadResult.Failure(failedModels)
        }
    }

    /**
     * Increase the number of successfully downloaded models if download is successful.
     */
    private fun onDownloadSuccess(
        mutex: Mutex,
        successDownloadedCount: MutableStateFlow<Int>,
    ) {
        CoroutineScope(mainDispatcher).launch {
            mutex.withLock {
                successDownloadedCount.value++
            }
        }
    }

    /**
     * Handle download failure.
     */
    private fun onDownloadFailure(
        ex: Exception,
        notEnoughSpace: MutableStateFlow<Boolean>,
    ) {
        if (ex is MlKitException) {
            if (ex.errorCode == MlKitException.NOT_ENOUGH_SPACE) {
                notEnoughSpace.value = true
            } else {
                Timber.d(ex.errorCode.toString())
                // TODO Until implement handling, make it crash to log what happened.
                throw ex
            }
        } else {
            Timber.d("ex is ${ex.javaClass.name}")
            // TODO Until implement handling, make it crash to log what happened.
            throw ex
        }
    }

    /**
     * Download user locale model.
     */
    private fun downloadUserLocaleModel(
        conditions: DownloadConditions,
        notEnoughSpace: MutableStateFlow<Boolean>,
        mutex: Mutex,
    ): Deferred<Void> {
        val options =
            TranslatorOptions.Builder()
                .setSourceLanguage(
                    TranslateLanguage.ENGLISH,
                )
                .setTargetLanguage(Locale.getDefault().toLanguageTag().substring(0, 2))
                .build()
        val translator: Translator = Translation.getClient(options)

        return translator.downloadModelIfNeeded(conditions)
            .addOnFailureListener {
                onDownloadFailure(
                    ex = it,
                    notEnoughSpace = notEnoughSpace,
                )
            }
            .addOnSuccessListener {
                onDownloadSuccess(
                    mutex = mutex,
                    successDownloadedCount = MutableStateFlow(0),
                )
            }
            .asDeferred()
    }

    /**
     * Delete language translation models.
     * Delete runs in parallel.
     * @param targetLanguages which to delete from the user device.
     */
    override suspend fun deleteModel(targetLanguages: List<Locale>) {
        val modelManager = RemoteModelManager.getInstance()

        targetLanguages.forEach { locale ->
            val model = TranslateRemoteModel.Builder(locale.toLanguageTag()).build()
            withContext(ioDispatcher) {
                launch {
                    modelManager.deleteDownloadedModel(model).await()
                }
            }
        }
    }
}
