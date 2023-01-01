package com.wiryadev.kotranslate.android.di

import android.app.Application
import com.squareup.sqldelight.db.SqlDriver
import com.wiryadev.kotranslate.database.TranslateDatabase
import com.wiryadev.kotranslate.translate.data.history.SqlDelightHistoryDataSource
import com.wiryadev.kotranslate.translate.data.local.DatabaseDriverFactory
import com.wiryadev.kotranslate.translate.data.remote.HttpClientFactory
import com.wiryadev.kotranslate.translate.data.translate.KtorTranslateClient
import com.wiryadev.kotranslate.translate.domain.history.HistoryDataSource
import com.wiryadev.kotranslate.translate.domain.translate.Translate
import com.wiryadev.kotranslate.translate.domain.translate.TranslateClient
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.ktor.client.*
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun providesHttpClient(): HttpClient = HttpClientFactory().create()

    @Provides
    @Singleton
    fun providesTranslateClient(
        httpClient: HttpClient
    ): TranslateClient = KtorTranslateClient(httpClient)

    @Provides
    @Singleton
    fun providesDatabaseDriverFactory(
        application: Application
    ): SqlDriver = DatabaseDriverFactory(application).create()

    @Provides
    @Singleton
    fun providesHistoryDataSource(
        driver: SqlDriver
    ): HistoryDataSource = SqlDelightHistoryDataSource(TranslateDatabase(driver))

    @Provides
    @Singleton
    fun providesTranslateUseCase(
        client: TranslateClient,
        dataSource: HistoryDataSource,
    ): Translate = Translate(client, dataSource)


}