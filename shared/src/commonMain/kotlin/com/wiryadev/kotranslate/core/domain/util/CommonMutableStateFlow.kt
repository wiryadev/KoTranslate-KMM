package com.wiryadev.kotranslate.core.domain.util

import kotlinx.coroutines.flow.MutableStateFlow

expect open class CommonMutableStateFlow<T>(flow: MutableStateFlow<T>) : MutableStateFlow<T>

fun <T> MutableStateFlow<T>.asCommonMutableStateFlow() = CommonMutableStateFlow(this)