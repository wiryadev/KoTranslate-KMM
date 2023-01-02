package com.wiryadev.kotranslate.android.translate.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowDropDown
import androidx.compose.material.icons.rounded.ArrowDropUp
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.wiryadev.kotranslate.android.core.theme.LightBlue
import com.wiryadev.kotranslate.core.presentation.UiLanguage
import com.wiryadev.kotranslate.android.R

@Composable
fun LanguageDropDown(
    selectedLanguage: UiLanguage,
    isOpen: Boolean,
    onClick: () -> Unit,
    onDismiss: () -> Unit,
    onSelectLanguage: (UiLanguage) -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(modifier = modifier) {
        DropdownMenu(
            expanded = isOpen,
            onDismissRequest = onDismiss,
        ) {
            UiLanguage.allLanguages.forEach { language ->
                LanguageDropDownItem(
                    language = language,
                    onClick = { onSelectLanguage(language) },
                    modifier = Modifier.fillMaxWidth(),
                )
            }
        }
    }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .clickable(onClick = onClick)
            .padding(16.dp),
    ) {
        AsyncImage(
            model = selectedLanguage.drawableRes,
            contentDescription = selectedLanguage.language.langName,
            modifier = Modifier.size(30.dp),
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = selectedLanguage.language.langName,
            color = LightBlue,
            overflow = TextOverflow.Clip,
        )
        Icon(
            imageVector = if (isOpen) {
                Icons.Rounded.ArrowDropUp
            } else Icons.Rounded.ArrowDropDown,
            contentDescription = stringResource(
                id = if (isOpen) R.string.close else R.string.open
            ),
            tint = LightBlue,
            modifier = Modifier.size(30.dp),
        )
    }
}

@Composable
fun LanguageDropDownItem(
    language: UiLanguage,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    DropdownMenuItem(
        onClick = onClick,
        modifier = modifier,
    ) {
        AsyncImage(
            model = language.drawableRes,
            contentDescription = language.language.langName,
            modifier = Modifier.size(40.dp)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = language.language.langName,
        )
    }
}