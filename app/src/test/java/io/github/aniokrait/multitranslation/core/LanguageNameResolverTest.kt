package io.github.aniokrait.multitranslation.core

import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import org.junit.Test
import java.util.Locale

class LanguageNameResolverTest {
    @Test
    fun testGetAllLanguagesName() {
        val result = LanguageNameResolver.getAllLanguagesLabel()

        assertEquals("アイスランド語", result[0])
        assertEquals("アイルランド語", result[1])
        assertEquals("日本語", result[56])
    }

    @Test
    fun testGetAllLanguagesLocale() {
        val result = LanguageNameResolver.getAllLanguagesLocale()


        assertTrue(result.contains(Locale.ENGLISH))
        assertTrue(result.contains(Locale.JAPANESE))
    }
}
