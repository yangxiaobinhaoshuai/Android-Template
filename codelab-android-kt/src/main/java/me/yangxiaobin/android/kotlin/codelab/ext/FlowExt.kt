package me.yangxiaobin.android.kotlin.codelab.ext

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.flow.scan
import kotlinx.coroutines.launch
import kotlin.collections.orEmpty
import kotlin.collections.plus
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext


public fun <T> Flow<T>.launchIn(
    scope: CoroutineScope,
    context: CoroutineContext = EmptyCoroutineContext,
): Job = scope.launch(context) {
    collect() // tail-call
}


public operator fun <T> Flow<T>.plus(other: Flow<T>): Flow<T> = merge(this, other)


fun <T, K> Flow<T>.groupBy(
    keySelector: (T) -> K
): Flow<Map<K, List<T>>> = scan(emptyMap()) { acc, value ->
    val key = keySelector(value)
    val list = acc[key].orEmpty() + value
    acc + (key to list)
}
