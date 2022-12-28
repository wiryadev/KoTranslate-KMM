package com.wiryadev.kotranslate.translate.domain.translate

import com.wiryadev.kotranslate.core.domain.language.Language

interface TranslateClient {

    suspend fun translate(
        text:String,
        fromLanguage: Language,
        toLanguage: Language,
    ): String
}