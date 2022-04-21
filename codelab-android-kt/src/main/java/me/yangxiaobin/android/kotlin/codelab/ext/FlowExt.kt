package me.yangxiaobin.android.kotlin.codelab.ext

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext


public fun <T> Flow<T>.launchIn(
    scope: CoroutineScope,
    context: CoroutineContext = EmptyCoroutineContext,
): Job = scope.launch(context) {
    collect() // tail-call
}
