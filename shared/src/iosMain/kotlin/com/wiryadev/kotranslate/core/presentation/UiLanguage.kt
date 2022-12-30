package com.wiryadev.kotranslate.core.presentation

import com.wiryadev.kotranslate.core.domain.language.Language

actual class UiLanguage(
    actual val language: Language,
    val imageName: String,
) {

    actual companion object {
        actual fun byCode(langCode: String): UiLanguage {
            return allLanguages.find {
                it.language.langCode == langCode
            } ?: throw IllegalArgumentException("Invalid or Unsupported Language")
        }

        actual val allLanguages: List<UiLanguage>
            get() = Language.values().map { language ->
                UiLanguage(
                    language = language,
                    imageName = language.langName.lowercase(),
                )
            }
    }
}