package io.github.aniokrait.multitranslation.core

import com.google.mlkit.nl.translate.TranslateLanguage
import java.util.Locale

class LanguageNameResolver {
    companion object {
        fun getAllLanguagesLabel(): List<Locale> {
            return TranslateLanguage.getAllLanguages().map {
                val locale = Locale.Builder().setLanguage(it).build()
                locale
            }
                .sortedBy { it.getDisplayLanguage(Locale.JAPANESE) } // TODO: Apply current system locale
        }

//        fun getAllLanguagesLocale(): List<Locale> {
//            return TranslateLanguage.getAllLanguages().map {
//                Locale.forLanguageTag(it)
//            }
//        }
    }
}
