package io.github.aniokrait.multitranslation.core

import junit.framework.TestCase.assertEquals
import org.junit.Test
import java.util.Locale

class LanguageNameResolverTest {
    @Test
    fun `Returns all available languages`() {
        val result = LanguageNameResolver.getAvailableLocales()

        assertEquals(59, result.size)
        assertEquals("アイスランド語", result[0].getDisplayLanguage(Locale.JAPANESE))
        assertEquals("アイルランド語", result[1].getDisplayLanguage(Locale.JAPANESE))
        assertEquals("日本語", result[56].getDisplayLanguage(Locale.JAPANESE))
    }
}
