package com.wiryadev.kotranslate.translate.data.translate

import com.wiryadev.kotranslate.NetworkConstants
import com.wiryadev.kotranslate.core.domain.language.Language
import com.wiryadev.kotranslate.translate.domain.translate.TranslateClient
import com.wiryadev.kotranslate.translate.domain.translate.TranslateError
import com.wiryadev.kotranslate.translate.domain.translate.TranslateException
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.utils.io.errors.*

class KtorTranslateClient(
    private val httpClient: HttpClient
) : TranslateClient {

    override suspend fun translate(
        text: String,
        fromLanguage: Language,
        toLanguage: Language,
    ): String {
        val result = try {
            httpClient.post {
                url("${NetworkConstants.BASE_URL}/translate")
                contentType(ContentType.Application.Json)
                setBody(
                    TranslateDto(
                        textToTranslate = text,
                        sourceLanguageCode = fromLanguage.langCode,
                        targetLanguageCode = toLanguage.langCode,
                    )
                )
            }
        } catch (e: IOException) {
            throw TranslateException(TranslateError.SERVICE_UNAVAILABLE)
        }
        when (result.status.value) {
            in 200..299 -> Unit
            in 400..499 -> throw TranslateException(TranslateError.CLIENT_ERROR)
            500 -> throw TranslateException(TranslateError.SERVER_ERROR)
            else -> throw TranslateException(TranslateError.UNKNOWN_ERROR)
        }

        return try {
            result.body<TranslatedDto>().translatedText
        } catch (e: Exception) {
            throw TranslateException(TranslateError.SERVER_ERROR)
        }
    }
}