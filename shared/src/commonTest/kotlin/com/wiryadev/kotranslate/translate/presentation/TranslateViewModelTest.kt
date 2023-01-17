package com.wiryadev.kotranslate.translate.presentation

import app.cash.turbine.test
import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isFalse
import assertk.assertions.isTrue
import com.wiryadev.kotranslate.core.presentation.UiLanguage
import com.wiryadev.kotranslate.translate.data.local.FakeHistoryDataSource
import com.wiryadev.kotranslate.translate.data.remote.FakeTranslateClient
import com.wiryadev.kotranslate.translate.domain.history.HistoryItem
import com.wiryadev.kotranslate.translate.domain.translate.Translate
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlin.test.BeforeTest
import kotlin.test.Test

class TranslateViewModelTest {

    private lateinit var viewModel: TranslateViewModel
    private lateinit var client: FakeTranslateClient
    private lateinit var dataSource: FakeHistoryDataSource

    @BeforeTest
    fun setUp() {
        client = FakeTranslateClient()
        dataSource = FakeHistoryDataSource()
        val translate = Translate(client, dataSource)
        viewModel = TranslateViewModel(
            translate = translate,
            historyDataSource = dataSource,
            coroutineScope = CoroutineScope(Dispatchers.Default)
        )
    }

    @Test
    fun `State and history items are properly combined`() = runBlocking {
        viewModel.state.test {
            val initialState = awaitItem()
            assertThat(initialState).isEqualTo(TranslateState())

            val item = HistoryItem(
                id = 0,
                fromLanguageCode = "en",
                fromText = "from",
                toLanguageCode = "id",
                toText = "dari"
            )

            dataSource.insertHistoryItem(item)
            val expected = UiHistoryItem(
                id = item.id!!,
                fromLanguage = UiLanguage.byCode(item.fromLanguageCode),
                fromText = item.fromText,
                toLanguage = UiLanguage.byCode(item.toLanguageCode),
                toText = item.toText,
            )
            val state = awaitItem()

            assertThat(state.history.first()).isEqualTo(expected)
        }
    }

    @Test
    fun `Translate Success - state properly updated`() = runBlocking {
        viewModel.state.test {
            awaitItem() // just to trigger collection of initial emission

            viewModel.onEvent(TranslateEvent.ChangeTranslationText("test"))
            awaitItem()

            viewModel.onEvent(TranslateEvent.Translate)
            val loadingState = awaitItem()
            assertThat(loadingState.isTranslating).isTrue()

            val resultState = awaitItem()
            assertThat(resultState.isTranslating).isFalse()
            assertThat(resultState.toText).isEqualTo(client.translatedText)
        }
    }

}