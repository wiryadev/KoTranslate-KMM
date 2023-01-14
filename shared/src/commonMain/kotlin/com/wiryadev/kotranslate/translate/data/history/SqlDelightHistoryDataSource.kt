package com.wiryadev.kotranslate.translate.data.history

import com.squareup.sqldelight.runtime.coroutines.asFlow
import com.squareup.sqldelight.runtime.coroutines.mapToList
import com.wiryadev.kotranslate.core.domain.util.CommonFlow
import com.wiryadev.kotranslate.core.domain.util.asCommonFlow
import com.wiryadev.kotranslate.database.TranslateDatabase
import com.wiryadev.kotranslate.translate.domain.history.HistoryDataSource
import com.wiryadev.kotranslate.translate.domain.history.HistoryItem
import database.HistoryEntity
import kotlinx.coroutines.flow.map
import kotlinx.datetime.Clock

class SqlDelightHistoryDataSource(
    db: TranslateDatabase
): HistoryDataSource {

    private val queries = db.translateQueries

    override fun getHistory(): CommonFlow<List<HistoryItem>> {
        return queries.getHistory()
            .asFlow()
            .mapToList()
            .map { it.map(HistoryEntity::toHistoryItem) }
            .asCommonFlow()
    }

    override suspend fun insertHistoryItem(item: HistoryItem) {
        queries.insertHistory(
            id = item.id,
            fromLanguageCode = item.fromLanguageCode,
            fromText = item.fromText,
            toLanguageCode = item.toLanguageCode,
            toText = item.toText,
            timestamp = Clock.System.now().toEpochMilliseconds(),

        )
    }
}