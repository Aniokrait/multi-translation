package io.github.aniokrait.multitranslation.datasource

import io.github.aniokrait.multitranslation.repository.HttpRequestResult
import io.ktor.client.HttpClient
import io.ktor.client.request.forms.submitForm
import io.ktor.client.statement.HttpResponse
import io.ktor.http.Parameters
import io.ktor.http.isSuccess
import io.ktor.http.parameters
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class KtorHttpClient(
    private val client: HttpClient,
    private val ioDispatcher: CoroutineDispatcher,
) : HttpClientInterface {
    override suspend fun submitForm(
        url: String,
        formParameters: Map<String, String>?,
    ): HttpRequestResult {
        return withContext(ioDispatcher) {
            val response: HttpResponse =
                client.submitForm(
                    url = url,
                    formParameters = formParameters?.let {
                        parameters {
                            it.forEach {
                                append(it.key, it.value)
                            }
                        }
                    } ?: Parameters.Empty,
                )

            return@withContext if (response.status.isSuccess()) {
                HttpRequestResult.Success
            } else {
                HttpRequestResult.Failure(message = "${response.status.value} : ${response.status.description}")
            }
        }
    }
}
