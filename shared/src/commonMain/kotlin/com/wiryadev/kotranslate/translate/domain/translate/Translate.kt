package com.wiryadev.kotranslate.translate.domain.translate

import com.wiryadev.kotranslate.core.domain.language.Language
import com.wiryadev.kotranslate.core.domain.util.Resource
import com.wiryadev.kotranslate.translate.domain.history.HistoryDataSource
import com.wiryadev.kotranslate.translate.domain.history.HistoryItem

class Translate(
    private val client: TranslateClient,
    private val historyDataSource: HistoryDataSource,
) {

    suspend operator fun invoke(
        text: String,
        fromLanguage: Language,
        toLanguage: Language,
    ): Resource<String> = try {
        val translatedText = client.translate(
            text = text,
            fromLanguage = fromLanguage,
            toLanguage = toLanguage,
        )
        historyDataSource.insertHistoryItem(
            HistoryItem(
                id = 0,
                fromLanguageCode = fromLanguage.langCode,
                fromText = text,
                toLanguageCode = toLanguage.langCode,
                toText = translatedText,
            )
        )
        Resource.Success(data = translatedText)
    } catch (e: TranslateException) {
        e.printStackTrace()
        Resource.Error(throwable = e)
    }
}