package com.wiryadev.kotranslate.android.translate.presentation.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import com.wiryadev.kotranslate.android.R
import com.wiryadev.kotranslate.android.core.theme.LightBlue
import com.wiryadev.kotranslate.core.presentation.UiLanguage

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun TranslateTextField(
    fromText: String,
    toText: String?,
    fromLanguage: UiLanguage,
    toLanguage: UiLanguage,
    isTranslating: Boolean,
    onTextChange: (String) -> Unit,
    onTranslateClick: () -> Unit,
    onCopyClick: (String) -> Unit,
    onCloseClick: () -> Unit,
    onSpeakerClick: () -> Unit,
    onTextFieldClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .shadow(
                elevation = 4.dp,
                shape = RoundedCornerShape(16.dp),
            )
            .clip(RoundedCornerShape(16.dp))
            .gradientSurface()
            .clickable(onClick = onTextFieldClick)
            .padding(16.dp)
    ) {
        AnimatedContent(
            targetState = toText == null || isTranslating,
        ) { shouldShowIdleState ->
            if (shouldShowIdleState) {
                IdleTranslateTextField(
                    fromText = fromText,
                    isTranslating = isTranslating,
                    onTextChange = onTextChange,
                    onTranslateClick = onTranslateClick,
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(2f),
                )
            } else {
                TranslatedTextField(
                    fromText = fromText,
                    toText = toText.orEmpty(),
                    fromLanguage = fromLanguage,
                    toLanguage = toLanguage,
                    onCopyClick = onCopyClick,
                    onSpeakerClick = onSpeakerClick,
                    onCloseClick = onCloseClick,
                    modifier = Modifier.fillMaxWidth(),
                )
            }
        }
    }
}

@Composable
private fun IdleTranslateTextField(
    fromText: String,
    isTranslating: Boolean,
    onTextChange: (String) -> Unit,
    onTranslateClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var isFocused by remember {
        mutableStateOf(false)
    }

    Box(modifier = modifier) {
        BasicTextField(
            value = fromText,
            onValueChange = onTextChange,
            textStyle = TextStyle(
                color = MaterialTheme.colors.onSurface,
            ),
            cursorBrush = SolidColor(MaterialTheme.colors.primary),
            modifier = Modifier
                .fillMaxSize()
                .onFocusChanged { isFocused = it.isFocused },
        )

        if (fromText.isEmpty() && !isFocused) {
            Text(
                text = stringResource(id = R.string.translate_text_hint),
                color = LightBlue,
            )
        }

        ProgressButton(
            text = stringResource(id = R.string.translate),
            isLoading = isTranslating,
            onClick = onTranslateClick,
            modifier = Modifier.align(Alignment.BottomEnd),
        )
    }
}

@Composable
private fun TranslatedTextField(
    fromText: String,
    toText: String,
    fromLanguage: UiLanguage,
    toLanguage: UiLanguage,
    onCopyClick: (String) -> Unit,
    onSpeakerClick: () -> Unit,
    onCloseClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
    ) {
        LanguageDisplay(language = fromLanguage)
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = fromText,
            color = MaterialTheme.colors.onSurface,
        )
        Spacer(modifier = Modifier.height(16.dp))
        Row(
            modifier = Modifier.align(Alignment.End),
        ) {
            IconButton(
                onClick = { onCopyClick(fromText) }
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(id = R.drawable.copy),
                    contentDescription = stringResource(id = R.string.copy),
                    tint = LightBlue,
                )
            }
            IconButton(onClick = onCloseClick) {
                Icon(
                    imageVector = Icons.Rounded.Close,
                    contentDescription = stringResource(id = R.string.close),
                    tint = LightBlue,
                )
            }
        }
        Spacer(modifier = Modifier.height(16.dp))

        Divider()

        Spacer(modifier = Modifier.height(16.dp))
        LanguageDisplay(language = toLanguage)
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = toText,
            color = MaterialTheme.colors.onSurface,
        )
        Spacer(modifier = Modifier.height(16.dp))
        Row(
            modifier = Modifier.align(Alignment.End),
        ) {
            IconButton(
                onClick = { onCopyClick(toText) }
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(id = R.drawable.copy),
                    contentDescription = stringResource(id = R.string.copy),
                    tint = LightBlue,
                )
            }
            IconButton(onClick = onSpeakerClick) {
                Icon(
                    imageVector = ImageVector.vectorResource(id = R.drawable.speaker),
                    contentDescription = stringResource(id = R.string.speak_text),
                    tint = LightBlue,
                )
            }
        }
    }
}