package io.github.aniokrait.multitranslation.core

import junit.framework.TestCase.assertEquals
import org.junit.Test
import java.util.Locale

class LocaleTest {
    @Test
    fun test() {
        val ja = Locale.JAPANESE
        val locale = Locale.forLanguageTag(ja.language)
        val jaTag = locale.toLanguageTag()

        assertEquals(ja, locale)
        assertEquals("ja", jaTag)
    }
}
