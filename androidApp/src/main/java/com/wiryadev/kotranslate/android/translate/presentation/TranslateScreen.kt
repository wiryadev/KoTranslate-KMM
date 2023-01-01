package com.wiryadev.kotranslate.android.translate.presentation

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.wiryadev.kotranslate.android.translate.presentation.components.LanguageDropDown
import com.wiryadev.kotranslate.android.translate.presentation.components.SwapLanguageButton
import com.wiryadev.kotranslate.translate.presentation.TranslateEvent
import com.wiryadev.kotranslate.translate.presentation.TranslateState

@Composable
fun TranslateScreen(
    state: TranslateState,
    onEvent: (TranslateEvent) -> Unit,
) {
    Scaffold(
        floatingActionButton = {},
    ) { paddingValues ->
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .padding(16.dp),
        ) {
            item {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    LanguageDropDown(
                        selectedLanguage = state.fromLanguage,
                        isOpen = state.isChoosingFromLanguage,
                        onClick = {
                            onEvent(TranslateEvent.OpenFromLanguageDropDown)
                        },
                        onDismiss = {
                            onEvent(TranslateEvent.StopChoosingLanguage)
                        },
                        onSelectLanguage = { language ->
                            onEvent(TranslateEvent.ChooseFromLanguage(language))
                        },
                    )
                    SwapLanguageButton(
                        onClick = { onEvent(TranslateEvent.SwapLanguage) },
                    )
                    LanguageDropDown(
                        selectedLanguage = state.toLanguage,
                        isOpen = state.isChoosingToLanguage,
                        onClick = {
                            onEvent(TranslateEvent.OpenToLanguageDropDown)
                        },
                        onDismiss = {
                            onEvent(TranslateEvent.StopChoosingLanguage)
                        },
                        onSelectLanguage = { language ->
                            onEvent(TranslateEvent.ChooseToLanguage(language))
                        },
                    )
                }
            }
        }

    }
}