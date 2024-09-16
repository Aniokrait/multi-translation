package io.github.aniokrait.multitranslation.datasource

import io.github.aniokrait.multitranslation.repository.HttpRequestResult
import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.http.HttpStatusCode
import io.mockk.mockk
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class KtorHttpClientTest {

    private lateinit var mockHttpClient: HttpClient
    private lateinit var testDispatcher: CoroutineDispatcher

    private val url = "https://this.is/test-url"

    @Before
    fun setUp() {
        mockHttpClient = mockk()
        testDispatcher = StandardTestDispatcher()
    }

    @Test
    fun `Return success if submit form success`() = runTest(testDispatcher) {
        val client = mockClient(HttpStatusCode.OK)
        val ktorHttpClient = KtorHttpClient(
            client = client,
            ioDispatcher = testDispatcher,
        )

        val result = ktorHttpClient.submitForm(
            url = url,
            formParameters = null,
        )

        assertTrue(result is HttpRequestResult.Success)
    }

    @Test
    fun `Return failure message if submit form fail`() = runTest(testDispatcher) {
        val client = mockClient(HttpStatusCode.InternalServerError)
        val ktorHttpClient = KtorHttpClient(
            client = client,
            ioDispatcher = testDispatcher,
        )

        val result = ktorHttpClient.submitForm(
            url = url,
            formParameters = null,
        )

        assertTrue(result is HttpRequestResult.Failure)
        assertEquals("500 : Internal Server Error", (result as HttpRequestResult.Failure).message)
    }

    private fun mockClient(status: HttpStatusCode): HttpClient {
        val engine = MockEngine {
            respond(
                content = "",
                status = status
            )
        }

        return HttpClient(engine)
    }

}
