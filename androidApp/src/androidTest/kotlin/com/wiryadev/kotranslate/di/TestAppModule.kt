package com.wiryadev.kotranslate.di

import com.wiryadev.kotranslate.translate.data.local.FakeHistoryDataSource
import com.wiryadev.kotranslate.translate.data.remote.FakeTranslateClient
import com.wiryadev.kotranslate.translate.domain.history.HistoryDataSource
import com.wiryadev.kotranslate.translate.domain.translate.Translate
import com.wiryadev.kotranslate.translate.domain.translate.TranslateClient
import com.wiryadev.kotranslate.voice_to_text.data.FakeVoiceToTextParser
import com.wiryadev.kotranslate.voice_to_text.domain.VoiceToTextParser
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object TestAppModule {

    @Provides
    @Singleton
    fun provideFakeTranslateClient(): TranslateClient = FakeTranslateClient()

    @Provides
    @Singleton
    fun provideFakeHistoryDataSource(): HistoryDataSource = FakeHistoryDataSource()

    @Provides
    @Singleton
    fun provideTranslateUseCase(
        client: TranslateClient,
        dataSource: HistoryDataSource
    ): Translate = Translate(
        client = client,
        historyDataSource = dataSource,
    )

    @Provides
    @Singleton
    fun provideFakeVoiceToTextParser(): VoiceToTextParser = FakeVoiceToTextParser()

}