package io.github.aniokrait.multitranslation.repository

interface InquiryRepository {
    suspend fun sendInquiry(content: String): HttpRequestResult

    fun getMetaInfo(): UserMetaInfo
}

data class UserMetaInfo(
    val sdk: Int,
    val model: String,
    val manufacturer: String,
    val product: String,
    val appVersion: Int,
)
