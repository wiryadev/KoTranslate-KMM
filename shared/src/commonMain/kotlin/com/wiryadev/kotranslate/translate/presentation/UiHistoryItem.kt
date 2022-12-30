package com.wiryadev.kotranslate.translate.presentation

import com.wiryadev.kotranslate.core.presentation.UiLanguage

data class UiHistoryItem(
    val id: Long,
    val fromLanguage: UiLanguage,
    val fromText: String,
    val toLanguage: UiLanguage,
    val toText: String,
)
