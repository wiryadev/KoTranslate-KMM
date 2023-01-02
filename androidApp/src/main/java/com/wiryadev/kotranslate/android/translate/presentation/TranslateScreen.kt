package com.wiryadev.kotranslate.android.translate.presentation

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.unit.dp
import com.wiryadev.kotranslate.android.translate.presentation.components.LanguageDropDown
import com.wiryadev.kotranslate.android.translate.presentation.components.SwapLanguageButton
import com.wiryadev.kotranslate.android.translate.presentation.components.TranslateTextField
import com.wiryadev.kotranslate.translate.presentation.TranslateEvent
import com.wiryadev.kotranslate.translate.presentation.TranslateState
import com.wiryadev.kotranslate.android.R

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun TranslateScreen(
    state: TranslateState,
    onEvent: (TranslateEvent) -> Unit,
) {
    val context = LocalContext.current

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
            // Language Selection Row
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

            // Translation Text Field
            item {
                val clipboardManager = LocalClipboardManager.current
                val keyboardController = LocalSoftwareKeyboardController.current

                TranslateTextField(
                    fromText = state.fromText,
                    toText = state.toText,
                    fromLanguage = state.fromLanguage,
                    toLanguage = state.toLanguage,
                    isTranslating = state.isTranslating,
                    onTextChange = { newText ->
                        onEvent(TranslateEvent.ChangeTranslationText(newText))
                    },
                    onTranslateClick = {
                        keyboardController?.hide()
                        onEvent(TranslateEvent.Translate)
                    },
                    onCopyClick = { text ->
                        clipboardManager.setText(
                            buildAnnotatedString {
                                append(text)
                            }
                        )
                        Toast.makeText(
                            context,
                            context.getString(R.string.copied_to_clipboard),
                            Toast.LENGTH_LONG
                        ).show()
                    },
                    onCloseClick = {
                        onEvent(TranslateEvent.CloseTranslation)
                    },
                    onSpeakerClick = {

                    },
                    onTextFieldClick = {
                        onEvent(TranslateEvent.EditTranslation)
                    },
                    modifier = Modifier.fillMaxWidth(),
                )
            }
        }

    }
}