package com.wiryadev.kotranslate.voice_to_text.data

import com.wiryadev.kotranslate.core.domain.util.CommonStateFlow
import com.wiryadev.kotranslate.core.domain.util.asCommonStateFlow
import com.wiryadev.kotranslate.voice_to_text.domain.VoiceToTextParser
import com.wiryadev.kotranslate.voice_to_text.domain.VoiceToTextParserState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

class FakeVoiceToTextParser : VoiceToTextParser {

    private val _state = MutableStateFlow(VoiceToTextParserState())
    override val state: CommonStateFlow<VoiceToTextParserState>
        get() = _state.asCommonStateFlow()

    var voiceResult = "voice result"

    override fun startListening(languageCode: String) {
        _state.update {
            it.copy(
                result = "",
                isSpeaking = true,
            )
        }
    }

    override fun stopListening() {
        _state.update {
            it.copy(
                result = voiceResult,
                isSpeaking = false,
            )
        }
    }

    override fun cancel() {
        // no op
    }

    override fun reset() {
        _state.update { VoiceToTextParserState() }
    }
}