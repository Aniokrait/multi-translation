package io.github.aniokrait.multitranslation.datasource.fake

import io.github.aniokrait.multitranslation.datasource.HttpClientInterface
import io.github.aniokrait.multitranslation.repository.HttpRequestResult

class FakeHttpClient() : HttpClientInterface {
    override suspend fun submitForm(
        url: String,
        formParameters: Map<String, String>,
    ): HttpRequestResult {
        TODO("Not yet implemented")
    }
}
