package io.github.aniokrait.multitranslation.viewmodel

import com.google.mlkit.nl.translate.TranslateLanguage
import com.google.mlkit.nl.translate.Translation
import com.google.mlkit.nl.translate.Translator
import com.google.mlkit.nl.translate.TranslatorOptions
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.test.runTest
import org.junit.AfterClass
import org.junit.BeforeClass
import org.junit.Test

class TransactionViewModelTest {

    companion object {
        lateinit var japaneseEnglishTranslator: Translator

        @BeforeClass
        @JvmStatic
        fun setUp() = runTest {
            val options = TranslatorOptions.Builder()
                .setSourceLanguage(TranslateLanguage.JAPANESE)
                .setTargetLanguage(TranslateLanguage.ENGLISH)
                .build()
            japaneseEnglishTranslator = Translation.getClient(options)
            japaneseEnglishTranslator.downloadModelIfNeeded().await()
        }

        @AfterClass
        @JvmStatic
        fun tearDown() {
            japaneseEnglishTranslator.close()
        }
    }

    @Test
    fun test() = runTest {
        val result = japaneseEnglishTranslator.translate("ハロー").await()
        assertEquals("Hello", result)
    }
}
