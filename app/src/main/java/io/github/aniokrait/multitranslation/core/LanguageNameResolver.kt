package io.github.aniokrait.multitranslation.core

import com.google.mlkit.nl.translate.TranslateLanguage
import java.util.Locale

class LanguageNameResolver {
    companion object {
        fun getAllLanguagesLabel(): List<String> {
            return TranslateLanguage.getAllLanguages().map {
                val locale = Locale.forLanguageTag(it)
                locale.getDisplayLanguage(Locale.JAPANESE)
            }.sorted()
        }

        fun getAllLanguagesLocale(): List<Locale> {
            return TranslateLanguage.getAllLanguages().map {
                Locale.forLanguageTag(it)
            }
        }
    }
}
