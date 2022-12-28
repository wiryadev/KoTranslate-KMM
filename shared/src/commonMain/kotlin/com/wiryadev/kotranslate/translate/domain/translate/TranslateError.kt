package com.wiryadev.kotranslate.translate.domain.translate

enum class TranslateError {
    SERVICE_UNAVAILABLE,
    CLIENT_ERROR,
    SERVER_ERROR,
    UNKNOWN_ERROR,
}

class TranslateException(
    error: TranslateError,
) : Exception(
    message = "A translation error occurred: $error"
)