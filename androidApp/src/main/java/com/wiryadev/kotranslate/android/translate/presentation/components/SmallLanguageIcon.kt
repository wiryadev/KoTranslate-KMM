package com.wiryadev.kotranslate.android.translate.presentation.components

import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.wiryadev.kotranslate.core.presentation.UiLanguage

@Composable
fun SmallLanguageIcon(
    language: UiLanguage,
    modifier: Modifier = Modifier,
) {
    AsyncImage(
        model = language.drawableRes,
        contentDescription = language.language.langName,
        modifier = modifier.size(24.dp)
    )
}