package io.github.aniokrait.multitranslation.datasource.fake

import io.github.aniokrait.multitranslation.datasource.HttpClientInterface
import io.github.aniokrait.multitranslation.repository.HttpRequestResult

class FakeHttpClient() : HttpClientInterface {
    var fail = false

    override suspend fun submitForm(
        url: String,
        formParameters: Map<String, String>,
    ): HttpRequestResult {
        return if (fail) {
            HttpRequestResult.Failure(message = "failed")
        } else {
            HttpRequestResult.Success
        }
    }
}
