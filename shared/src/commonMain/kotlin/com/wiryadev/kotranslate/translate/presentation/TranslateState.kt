package com.wiryadev.kotranslate.translate.presentation

import com.wiryadev.kotranslate.core.presentation.UiLanguage
import com.wiryadev.kotranslate.translate.domain.translate.TranslateError

data class TranslateState(
    val fromText: String = "",
    val toText: String? = null,
    val fromLanguage: UiLanguage = UiLanguage.byCode("en"),
    val toLanguage: UiLanguage = UiLanguage.byCode("id"),
    val isChoosingFromLanguage: Boolean = false,
    val isChoosingToLanguage: Boolean = false,
    val isTranslating: Boolean = false,
    val error: TranslateError? = null,
    val history: List<UiHistoryItem> = emptyList(),
)
