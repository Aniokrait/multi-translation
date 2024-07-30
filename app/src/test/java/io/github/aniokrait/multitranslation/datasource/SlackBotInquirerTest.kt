package io.github.aniokrait.multitranslation.datasource

import io.github.aniokrait.multitranslation.datasource.fake.FakeHttpClient
import io.github.aniokrait.multitranslation.repository.HttpRequestResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.runTest
import org.junit.Test

class SlackBotInquirerTest {
    @Test
    fun testSendInquiry() = runTest {
        val client = FakeHttpClient()
        val slackBotInquirer = SlackBotInquirer(
            client = client,
            ioDispatcher = Dispatchers.IO
        )
        val content = "テスト問い合わせ"
        val result = slackBotInquirer.sendInquiry(content = content)
        assert(result is HttpRequestResult.Success)
    }
}
