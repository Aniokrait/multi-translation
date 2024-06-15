package io.github.aniokrait.multitranslation.core

import junit.framework.TestCase.assertEquals
import org.junit.Test
import java.util.Locale

class LanguageNameResolverTest {
    @Test
    fun testGetAllLanguagesName() {
        val result = LanguageNameResolver.getAllLanguagesLabel()

        assertEquals("アイスランド語", result[0].getDisplayLanguage(Locale.JAPANESE))
        assertEquals("アイルランド語", result[1].getDisplayLanguage(Locale.JAPANESE))
        assertEquals("日本語", result[56].getDisplayLanguage(Locale.JAPANESE))
    }

//    @Test
//    fun testGetAllLanguagesLocale() {
//        val result = LanguageNameResolver.getAllLanguagesLocale()
//
//
//        assertTrue(result.contains(Locale.ENGLISH))
//        assertTrue(result.contains(Locale.JAPANESE))
//    }
}
