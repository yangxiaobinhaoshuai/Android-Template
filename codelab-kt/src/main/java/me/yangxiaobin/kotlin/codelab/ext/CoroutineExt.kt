package me.yangxiaobin.kotlin.codelab.ext

import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext


fun <T> debounce(
    delayMs: Long = 500L,
    coroutineContext: CoroutineContext = EmptyCoroutineContext,
    f: (T) -> Unit
): (T) -> Unit {
    var debounceJob: Job? = null
    return { param: T ->
        if (debounceJob?.isCompleted != false) {
            debounceJob = CoroutineScope(coroutineContext).launch {
                delay(delayMs)
                f(param)
            }
        }
    }
}

fun <T> debounce(
    delayMs: Long = 500L,
    coroutineScope: CoroutineScope = CoroutineScope(Dispatchers.Main),
    f: (T) -> Unit
): (T) -> Unit {
    var debounceJob: Job? = null
    return { param: T ->
        if (debounceJob?.isCompleted != false) {
            debounceJob = coroutineScope.launch {
                delay(delayMs)
                f(param)
            }
        }
    }
}

fun launchTimer(xMs: Long, coroutineScope: CoroutineScope, f: () -> Unit): Job {
    return coroutineScope.launch {
        while (true) {
            delay(xMs)
            f()
        }
    }
}
