package io.github.aniokrait.multitranslation.datasource

import io.github.aniokrait.multitranslation.repository.HttpRequestResult

interface HttpClientInterface {
    suspend fun submitForm(
        url: String,
        formParameters: Map<String, String>,
    ): HttpRequestResult
}
