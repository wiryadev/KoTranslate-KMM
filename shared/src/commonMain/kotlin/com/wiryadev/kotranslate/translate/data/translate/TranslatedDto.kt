package com.wiryadev.kotranslate.translate.data.translate

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TranslatedDto(
    @SerialName("translatedText")
    val translatedText: String,
)
