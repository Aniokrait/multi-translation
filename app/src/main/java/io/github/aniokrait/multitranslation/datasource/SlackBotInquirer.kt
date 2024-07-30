package io.github.aniokrait.multitranslation.datasource

import io.github.aniokrait.multitranslation.BuildConfig
import io.github.aniokrait.multitranslation.repository.HttpRequestResult
import io.github.aniokrait.multitranslation.repository.InquiryRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class SlackBotInquirer(
    private val client: HttpClientInterface,
    private val ioDispatcher: CoroutineDispatcher,
) : InquiryRepository {
    companion object {
        private const val SLACK_API_TOKEN = BuildConfig.SLACK_API_KEY
        private const val CHANNEL_NAME = "#問い合わせ"
    }

    override suspend fun sendInquiry(content: String): HttpRequestResult {
        return withContext(ioDispatcher) {
            val response: HttpRequestResult = client.submitForm(
                url = "https://slack.com/api/chat.postMessage",
                formParameters = mapOf(
                    "token" to SLACK_API_TOKEN,
                    "channel" to CHANNEL_NAME,
                    "text" to content
                )
            )

            return@withContext response
        }
    }
}
