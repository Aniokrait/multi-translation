package io.github.aniokrait.multitranslation.repository

sealed interface HttpRequestResult {
    data object Success : HttpRequestResult
    data class Failure(val message: String) : HttpRequestResult
}
