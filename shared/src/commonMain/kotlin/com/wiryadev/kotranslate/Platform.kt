package com.wiryadev.kotranslate

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform