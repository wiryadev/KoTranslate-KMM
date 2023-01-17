package com.wiryadev.kotranslate.translate.data.remote

import com.wiryadev.kotranslate.core.domain.language.Language
import com.wiryadev.kotranslate.translate.domain.translate.TranslateClient

class FakeTranslateClient : TranslateClient {

    var translatedText = "test translation"

    override suspend fun translate(
        text: String,
        fromLanguage: Language,
        toLanguage: Language
    ): String {
        return translatedText
    }
}