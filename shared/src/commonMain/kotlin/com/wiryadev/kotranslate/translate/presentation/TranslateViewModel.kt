package com.wiryadev.kotranslate.translate.presentation

import com.wiryadev.kotranslate.core.domain.util.Resource
import com.wiryadev.kotranslate.core.domain.util.toCommonStateFlow
import com.wiryadev.kotranslate.core.presentation.UiLanguage
import com.wiryadev.kotranslate.translate.domain.history.HistoryDataSource
import com.wiryadev.kotranslate.translate.domain.translate.Translate
import com.wiryadev.kotranslate.translate.domain.translate.TranslateException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class TranslateViewModel(
    private val translate: Translate,
    historyDataSource: HistoryDataSource,
    coroutineScope: CoroutineScope?,
) {

    private val viewModelScope = coroutineScope ?: CoroutineScope(Dispatchers.Main)

    private val _state = MutableStateFlow(TranslateState())
    val state = combine(
        _state,
        historyDataSource.getHistory()
    ) { state, history ->
        if (state.history != history) {
            state.copy(
                history = history.map { item ->
                    UiHistoryItem(
                        id = item.id,
                        fromLanguage = UiLanguage.byCode(item.fromLanguageCode),
                        fromText = item.fromText,
                        toLanguage = UiLanguage.byCode(item.toLanguageCode),
                        toText = item.toText,
                    )
                }
            )
        } else {
            state
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = TranslateState(),
    ).toCommonStateFlow()

    private var translateJob: Job? = null

    fun onEvent(event: TranslateEvent) {
        when (event) {
            is TranslateEvent.ChangeTranslationText -> {
                _state.update {
                    it.copy(fromText = event.text)
                }
            }
            is TranslateEvent.ChooseFromLanguage -> {
                _state.update {
                    it.copy(
                        isChoosingFromLanguage = false,
                        fromLanguage = event.language,
                    )
                }
            }
            is TranslateEvent.ChooseToLanguage -> {
                val newState = _state.updateAndGet {
                    it.copy(
                        isChoosingToLanguage = false,
                        toLanguage = event.language,
                    )
                }
                translate(newState)
            }
            TranslateEvent.CloseTranslation -> {
                _state.update {
                    it.copy(
                        isTranslating = false,
                        fromText = "",
                        toText = null,
                    )
                }
            }
            TranslateEvent.EditTranslation -> {
                if (state.value.toText != null) {
                    _state.update {
                        it.copy(
                            toText = null,
                            isTranslating = false,
                        )
                    }
                }
            }
            TranslateEvent.OnErrorSeen -> {
                _state.update {
                    it.copy(error = null)
                }
            }
            TranslateEvent.OpenFromLanguageDropDown -> {
                _state.update {
                    it.copy(
                        isChoosingFromLanguage = true,
                    )
                }
            }
            TranslateEvent.OpenToLanguageDropDown -> {
                _state.update {
                    it.copy(
                        isChoosingToLanguage = true,
                    )
                }
            }
            is TranslateEvent.SelectHistoryItem -> {
                translateJob?.cancel()
                _state.update {
                    it.copy(
                        fromText = event.item.fromText,
                        toText = event.item.toText,
                        fromLanguage = event.item.fromLanguage,
                        toLanguage = event.item.toLanguage,
                        isTranslating = false,
                    )
                }
            }
            TranslateEvent.StopChoosingLanguage -> {
                _state.update {
                    it.copy(
                        isChoosingToLanguage = false,
                        isChoosingFromLanguage = false,
                    )
                }
            }
            is TranslateEvent.SubmitVoiceResult -> {
                _state.update {
                    it.copy(
                        fromText = event.result ?: it.fromText,
                        isTranslating = if (event.result != null) false else it.isTranslating,
                        toText = if (event.result != null) null else it.toText,
                    )
                }
            }
            TranslateEvent.SwapLanguage -> {
                _state.update {
                    it.copy(
                        fromLanguage = it.toLanguage,
                        fromText = it.toText.orEmpty(),
                        toLanguage = it.fromLanguage,
                        toText = if (it.toText != null) it.fromText else null,
                    )
                }
            }
            TranslateEvent.Translate -> translate(state.value)
            else -> Unit
        }
    }

    private fun translate(state: TranslateState) {
        if (state.isTranslating
            || state.fromText.isBlank()
        ) return

        translateJob = viewModelScope.launch {
            _state.update { it.copy(isTranslating = true) }

            val result = translate(
                text = state.fromText,
                fromLanguage = state.fromLanguage.language,
                toLanguage = state.toLanguage.language,
            )

            when (result) {
                is Resource.Success -> {
                    _state.update {
                        it.copy(
                            isTranslating = false,
                            toText = result.data,
                        )
                    }
                }
                is Resource.Error -> {
                    _state.update {
                        it.copy(
                            isTranslating = false,
                            error = (result.throwable as? TranslateException)?.error,
                        )
                    }
                }
            }
        }
    }

}