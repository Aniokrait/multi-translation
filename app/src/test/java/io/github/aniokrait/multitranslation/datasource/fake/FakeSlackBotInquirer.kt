package io.github.aniokrait.multitranslation.datasource.fake

import io.github.aniokrait.multitranslation.repository.HttpRequestResult
import io.github.aniokrait.multitranslation.repository.InquiryRepository

class FakeSlackBotInquirer : InquiryRepository {
    var isSuccess = true
    var sentContent: String = ""

    override suspend fun sendInquiry(content: String): HttpRequestResult {
        sentContent = content

        return if (isSuccess) {
            HttpRequestResult.Success
        } else {
            HttpRequestResult.Failure("500 : 失敗しました")
        }
    }
}
