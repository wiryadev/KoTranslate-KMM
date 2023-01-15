package com.wiryadev.kotranslate.voice_to_text.domain

import com.wiryadev.kotranslate.core.domain.util.CommonStateFlow

interface VoiceToTextParser {

    val state: CommonStateFlow<VoiceToTextParserState>

    fun startListening(languageCode: String)
    fun stopListening()
    fun cancel()
    fun reset()
}