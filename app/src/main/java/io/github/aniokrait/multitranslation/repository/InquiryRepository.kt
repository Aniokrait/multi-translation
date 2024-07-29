package io.github.aniokrait.multitranslation.repository

interface InquiryRepository {
    suspend fun sendInquiry(content: String): HttpRequestResult
}


