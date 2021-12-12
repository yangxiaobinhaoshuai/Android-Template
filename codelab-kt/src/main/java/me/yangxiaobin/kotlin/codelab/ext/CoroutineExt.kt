package me.yangxiaobin.kotlin.codelab.ext

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext


fun <T> debounce(delayMs: Long = 500L,
                 coroutineContext: CoroutineContext,
                 f: (T) -> Unit): (T) -> Unit {
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

fun launchTimer(xMs: Long, coroutineScope: CoroutineScope, f: () -> Unit): Job {
    return coroutineScope.launch {
        while (true) {
            delay(xMs)
            f()
        }
    }
}
