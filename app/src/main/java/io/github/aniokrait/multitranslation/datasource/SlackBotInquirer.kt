package io.github.aniokrait.multitranslation.datasource

import io.github.aniokrait.multitranslation.BuildConfig
import io.github.aniokrait.multitranslation.repository.HttpRequestResult
import io.github.aniokrait.multitranslation.repository.InquiryRepository
import io.ktor.client.HttpClient
import io.ktor.client.request.forms.submitForm
import io.ktor.client.statement.HttpResponse
import io.ktor.http.isSuccess
import io.ktor.http.parameters
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class SlackBotInquirer(
    private val client: HttpClient,
    private val ioDispatcher: CoroutineDispatcher,
) : InquiryRepository {
    companion object {
        private const val SLACK_API_TOKEN = BuildConfig.SLACK_API_KEY
        private const val CHANNEL_NAME = "#問い合わせ"
    }

    override suspend fun sendInquiry(content: String): HttpRequestResult {
        return withContext(ioDispatcher) {
            val response: HttpResponse = client.submitForm(
                url = "https://slack.com/api/chat.postMessage",
                formParameters = parameters {
                    append("token", SLACK_API_TOKEN)
                    append("channel", CHANNEL_NAME)
                    append("text", content)
                }
            )

            return@withContext if (!response.status.isSuccess()) {
                HttpRequestResult.Failure(message = "${response.status.value} : ${response.status.description}")
            } else {
                HttpRequestResult.Success
            }
        }
    }
}
