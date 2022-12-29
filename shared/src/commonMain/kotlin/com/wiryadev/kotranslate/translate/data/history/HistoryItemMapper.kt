package com.wiryadev.kotranslate.translate.data.history

import com.wiryadev.kotranslate.translate.domain.history.HistoryItem
import database.HistoryEntity

fun HistoryEntity.toHistoryItem() = HistoryItem(
    id = id,
    fromLanguageCode = fromLanguageCode,
    fromText = fromText,
    toLanguageCode = toLanguageCode,
    toText = toText,
)