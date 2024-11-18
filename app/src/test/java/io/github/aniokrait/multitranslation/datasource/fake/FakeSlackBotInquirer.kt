package io.github.aniokrait.multitranslation.datasource.fake

import io.github.aniokrait.multitranslation.repository.HttpRequestResult
import io.github.aniokrait.multitranslation.repository.InquiryRepository
import io.github.aniokrait.multitranslation.repository.UserMetaInfo

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

    override fun getMetaInfo(): UserMetaInfo {
        val sdk = 34
        val model = "Pixel 6"
        val manufacturer = "Google"
        val product = "Pixel 6"
        val appVersion = 1

        return UserMetaInfo(
            sdk = sdk,
            model = model,
            manufacturer = manufacturer,
            product = product,
            appVersion = appVersion,
        )
    }
}
