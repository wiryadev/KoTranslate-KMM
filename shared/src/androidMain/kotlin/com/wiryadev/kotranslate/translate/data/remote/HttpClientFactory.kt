package com.wiryadev.kotranslate.translate.data.remote

import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json

actual class HttpClientFactory {

    actual fun create(): HttpClient = HttpClient(Android) {
        install(ContentNegotiation) {
            json()
        }
    }
}