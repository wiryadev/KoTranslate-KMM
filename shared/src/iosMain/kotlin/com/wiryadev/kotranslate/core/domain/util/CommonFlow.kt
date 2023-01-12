package com.wiryadev.kotranslate.core.domain.util

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow

actual open class CommonFlow<T> actual constructor(
    private val flow: Flow<T>
) : Flow<T> by flow {

    fun subscribe(
        coroutineScope: CoroutineScope,
        dispatcher: CoroutineDispatcher,
        onCollect: (T) -> Unit,
    ): IosDisposableHandle {
        val job = coroutineScope.launch(dispatcher) {
            flow.collect(onCollect)
        }
        return IosDisposableHandle { job.cancel() }
    }

    fun subscribe(
        onCollect: (T) -> Unit,
    ): IosDisposableHandle {
        return subscribe(
            coroutineScope = GlobalScope,
            dispatcher = Dispatchers.Main,
            onCollect = onCollect,
        )
    }
}