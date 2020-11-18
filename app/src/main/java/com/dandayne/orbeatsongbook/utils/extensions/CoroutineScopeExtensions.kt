package com.dandayne.orbeatsongbook.utils.extensions

import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

inline fun CoroutineScope.launchInterval(
    initialDelayMillis: Long = 0,
    repeatMillis: Long = 0,
    context: CoroutineContext = EmptyCoroutineContext,
    start: CoroutineStart = CoroutineStart.DEFAULT,
    crossinline action: suspend CoroutineScope.() -> Unit
) = launch(context, start) {
    delay(initialDelayMillis)
    if (repeatMillis > 0) {
        while (isActive) {
            action()
            delay(repeatMillis)
        }
    } else if (isActive) {
        action()
    }
}