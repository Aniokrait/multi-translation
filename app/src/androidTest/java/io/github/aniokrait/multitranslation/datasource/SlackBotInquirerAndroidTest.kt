package io.github.aniokrait.multitranslation.datasource

import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.engine.cio.endpoint
import io.ktor.client.plugins.logging.Logging
import kotlinx.coroutines.Dispatchers
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.BeforeClass
import org.junit.Test

class SlackBotInquirerAndroidTest {
    private lateinit var slackBotInquirer: SlackBotInquirer

    companion object {
        private lateinit var client: KtorHttpClient

        @JvmStatic
        @BeforeClass
        fun setUpBeforeAll() {
            client = createHttpClient()
        }

        private fun createHttpClient(): KtorHttpClient {
            val httpClient =
                HttpClient(CIO) {
                    expectSuccess = true

                    engine {
                        endpoint {
                            keepAliveTime = 10000
                            connectTimeout = 10000
                            connectAttempts = 5
                        }
                    }
                    install(Logging)
                }

            return KtorHttpClient(client = httpClient, ioDispatcher = Dispatchers.IO)
        }
    }

    @Before
    fun setUp() {
        slackBotInquirer =
            SlackBotInquirer(
                client = client,
                ioDispatcher = Dispatchers.IO,
            )
    }

    @Test
    fun getMetaInfo() {
        val result = slackBotInquirer.getMetaInfo()

        assertEquals(33, result.sdk)
        assertEquals("sdk_gphone64_arm64", result.model) // Pixel emulator
        assertEquals("Google", result.manufacturer)
        assertEquals("sdk_gphone64_arm64", result.product)
        assertEquals(1, result.appVersion)
    }
}
