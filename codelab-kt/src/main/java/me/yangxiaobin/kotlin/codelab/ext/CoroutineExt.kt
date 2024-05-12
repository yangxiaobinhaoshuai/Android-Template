package me.yangxiaobin.kotlin.codelab.ext

import kotlinx.coroutines.*
import kotlin.coroutines.Continuation
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException


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


fun <T> Continuation<T>.resumeSafely(value: T) {
    val job: Job? = this.context[Job]
    if (job != null) {
        if (job.isActive) this.resume(value)
    }
}

fun <T> Continuation<T>.resumeWithExceptionSafely(exception: Throwable) {
    val job: Job? = this.context[Job]
    if (job != null) {
        if (job.isActive) this.resumeWithException(exception)
    }
}
