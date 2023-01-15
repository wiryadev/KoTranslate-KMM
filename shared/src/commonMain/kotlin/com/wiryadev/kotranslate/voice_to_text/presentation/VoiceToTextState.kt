package com.wiryadev.kotranslate.voice_to_text.presentation

data class VoiceToTextState(
    val powerRatios: List<Float> = emptyList(),
    val spokenText: String = "",
    val canRecord: Boolean = false,
    val recordError: String? = null,
    val displayState: DisplayState? = null,
)

enum class DisplayState {
    WAITING_TO_SPEAK,
    SPEAKING,
    DISPLAYING_RESULT,
    ERROR,
}
