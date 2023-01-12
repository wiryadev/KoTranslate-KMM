package com.wiryadev.kotranslate.di

import com.wiryadev.kotranslate.database.TranslateDatabase
import com.wiryadev.kotranslate.translate.data.history.SqlDelightHistoryDataSource
import com.wiryadev.kotranslate.translate.data.local.DatabaseDriverFactory
import com.wiryadev.kotranslate.translate.data.remote.HttpClientFactory
import com.wiryadev.kotranslate.translate.data.translate.KtorTranslateClient
import com.wiryadev.kotranslate.translate.domain.history.HistoryDataSource
import com.wiryadev.kotranslate.translate.domain.translate.Translate
import com.wiryadev.kotranslate.translate.domain.translate.TranslateClient

class AppModule {
    val historyDataSource: HistoryDataSource by lazy {
        SqlDelightHistoryDataSource(
            TranslateDatabase(
                DatabaseDriverFactory().create()
            )
        )
    }

    private val translateClient: TranslateClient by lazy {
        KtorTranslateClient(
            HttpClientFactory().create()
        )
    }

    val translate: Translate by lazy {
        Translate(
            client = translateClient,
            historyDataSource = historyDataSource,
        )
    }
}