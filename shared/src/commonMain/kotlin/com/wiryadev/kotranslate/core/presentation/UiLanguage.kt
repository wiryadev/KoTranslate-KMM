package com.wiryadev.kotranslate.core.presentation

import com.wiryadev.kotranslate.core.domain.language.Language

expect class UiLanguage {
    val language: Language
    companion object {
        fun byCode(langCode: String): UiLanguage
        val allLanguages: List<UiLanguage>
    }
}