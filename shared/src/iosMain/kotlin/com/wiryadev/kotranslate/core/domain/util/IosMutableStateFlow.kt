package com.wiryadev.kotranslate.core.domain.util

import kotlinx.coroutines.flow.MutableStateFlow

class IosMutableStateFlow<T>(
    initialValue: T,
): CommonMutableStateFlow<T>(MutableStateFlow(initialValue))