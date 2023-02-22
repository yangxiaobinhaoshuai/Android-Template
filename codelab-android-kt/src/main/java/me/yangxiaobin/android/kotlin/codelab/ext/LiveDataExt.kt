package me.yangxiaobin.android.kotlin.codelab.ext

import androidx.annotation.CheckResult
import androidx.annotation.IntRange
import androidx.annotation.MainThread
import androidx.annotation.UiThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData


@UiThread
@CheckResult
public fun <T> LiveData<T>.toMutable(): MutableLiveData<T> {

    if (this is MediatorLiveData) return this

    val mediator = MediatorLiveData<T>()
    mediator.addSource(this,mediator::setValue)
    return mediator
}

@UiThread
@CheckResult
public fun <T> LiveData<T>.onChanged(onChanged: (T) -> Unit): LiveData<T> {

    val mediator = MediatorLiveData<T>()

    this.observeForever(mediator::setValue)

    return mediator
}

// region buffer
@Suppress("UNCHECKED_CAST")
public fun <T> LiveData<T>.buffer(count: Int, skip: Int): LiveData<List<T>> {
    val buffer: Array<Any?> = arrayOfNulls(if (count > skip) skip else count)
    val result = MediatorLiveData<List<T>>()
    var counter = 0
    result.addSource(this) {
        if (counter < count) {
            buffer[counter] = it
        }
        counter++
        if (counter == skip) {
            counter = 0
            result.value = buffer.toList() as List<T>
        }
    }
    return result
}

public fun <T> LiveData<T>.buffer(count: Int): LiveData<List<T>> =
    buffer(count, count)
// endregion

//region CombineLatest
public fun <T1, T2, R> LiveData<T1>.combineLatestWith(
    other: LiveData<T2>,
    combiner: (T1, T2) -> R
): LiveData<R> = combineLatestArray(this, other, combiner)

@MainThread
@Suppress("UNCHECKED_CAST")
public fun <T1, T2, R> combineLatestArray(
    source1: LiveData<T1>,
    source2: LiveData<T2>,
    combiner: (T1, T2) -> R
): LiveData<R> {
    val func: (Array<Any>) -> R = { input: Array<Any> -> combiner(input[0] as T1, input[1] as T2) }
    return combineMultiLatest(source1 as LiveData<Any>, source2 as LiveData<Any>, combiner = func)
}

public fun <T, R> combineMultiLatest(
    vararg sources: LiveData<T>,
    combiner: (Array<T>) -> R,
): LiveData<R> = combineLatestArray(sources, combiner)

private val NOT_SET = Any()

@MainThread
@Suppress("UNCHECKED_CAST")
private fun <T, R> combineLatestArray(
    sources: Array<out LiveData<out T>>,
    combiner: (Array<T>) -> R,
): LiveData<R> {

    if (sources.isEmpty()) return MutableLiveData()

    val size = sources.size
    val result = MediatorLiveData<Any>()

    val values: Array<Any?> = arrayOfNulls<Any?>(size)
    for (index in 0 until size) values[index] = NOT_SET

    var emits = 0
    for (index in 0 until size) {

        val observer = { t: Any ->
            var combine = emits == size

            if (!combine) {
                if (values[index] === NOT_SET) emits++
                combine = emits == size
            }

            values[index] = t

            if (combine) result.value = combiner(values as Array<T>)
        }

        result.addSource(sources[index] as LiveData<Any>, observer)
    }
    return result as LiveData<R>
}
//endregion

/**
 * @see merge
 */
@UiThread
@CheckResult
public operator fun <T> LiveData<T>.plus(source: LiveData<T>): LiveData<T> = this.mergeWith(source)

public fun <T> LiveData<T>.mergeWith(vararg sources: LiveData<T>): LiveData<T> {
    val mediator = MediatorLiveData<T>()
    mediator.addSource(this, mediator::setValue)
    for (source in sources) mediator.addSource(source, mediator::setValue)
    return mediator
}

public fun <T> merge(vararg sources: LiveData<T>): LiveData<T> {
    val mediator = MediatorLiveData<T>()
    for (source in sources) mediator.addSource(source, mediator::setValue)
    return mediator
}


// region distinct
public fun <T> LiveData<T>.distinct(): LiveData<T> = distinct { value -> value }

public inline fun <T, K> LiveData<T>.distinct(crossinline func: (T) -> K): LiveData<T> {
    val keys = hashSetOf<K>()
    return filter { keys.add(func(it)) }
}

public inline fun <T, K> LiveData<T>.distinctUntilChanged(crossinline func: (T) -> K): LiveData<T> {
    var prev: Any? = null
    return filter {
        val key = func(it)
        if (key !== prev) {
            prev = key
            true
        } else {
            false
        }
    }
}
// endregion


//region Filter api
/**
 * 只监听获取LiveData的第一个值
 */
@CheckResult
public fun <T> LiveData<T>.first(): LiveData<T> = take(1)


/**
 * 只监听获取LiveData的 [count] 个值
 */
@CheckResult
public fun <T> LiveData<T>.take(@IntRange(from = 1, to = Long.MAX_VALUE) count: Int): LiveData<T> {
    require(count > 0) { "count must be greater than 1" }
    var counter = 0
    return takeWhile { counter++ < count }
}

/**
 * 只监听获取LiveData的 [count] 个值
 */
@CheckResult
public fun <T> LiveData<T>.drop(@IntRange(from = 1, to = Long.MAX_VALUE) count: Int): LiveData<T> {
    require(count > 0) { "count must be greater than 1" }
    var counter = 0
    return takeWhile { counter++ < count }
}


/**
 * 监听LiveData的值更新，直到 [predicate] 返回false
 */
@CheckResult
public inline fun <T> LiveData<T>.takeWhile(crossinline predicate: (T) -> Boolean): LiveData<T> {
    val result = MediatorLiveData<T>()
    result.addSource(this) { value ->
        if (predicate(value as T)) result.value = value
        else result.removeSource(this)
    }
    return result
}

@CheckResult
public fun <T> LiveData<T>.skip(count: Int): LiveData<T> {
    var counter = 0
    return skipWhile { ++counter > count }
}

@CheckResult
public inline fun <T> LiveData<T>.skipWhile(crossinline predicate: (T) -> Boolean): LiveData<T> {
    val result = MediatorLiveData<T>()
    var drop = true
    result.addSource(this) { value ->
        if (!drop || predicate(value)) {
            drop = false
            result.value = value
        }
    }
    return result
}

//endregion


// region filter 过滤相关
@CheckResult
public inline fun <T> LiveData<T>.filter(crossinline func: (T) -> Boolean): LiveData<T> {
    val result = MediatorLiveData<T>()
    result.addSource(this) { value ->
        if (func(value)) {
            result.value = value
        }
    }
    return result
}

public inline fun <T> LiveData<T>.filterNot(crossinline func: (T) -> Boolean): LiveData<T> = filter { value -> !func(value) }

@Suppress("UNCHECKED_CAST")
public fun <T> LiveData<T?>.filterNotNull(): LiveData<T> = filter { it != null } as LiveData<T>

@Suppress("UNCHECKED_CAST")
public inline fun <reified T> LiveData<Any>.filterIsInstance(): LiveData<T> = filter { it is T } as LiveData<T>
// endregion



//region Active & Inactive
/**
 * LiveData 触发 [LiveData.onActive] 回调
 */
@CheckResult
public inline fun <T> LiveData<T>.doOnActive(crossinline func: () -> Unit): LiveData<T> {
    val hook = object : LiveData<T>() {
        override fun onActive() = func()
    }

    val result = MediatorLiveData<T>()
    result.addSource(this) { result.value = it }
    result.addSource(hook) { }
    return result
}

/**
 * LiveData 触发 [LiveData.onInactive] 回调
 */
@CheckResult
public inline fun <T> LiveData<T>.doOnInactive(crossinline func: () -> Unit): LiveData<T> {
    val hook = object : LiveData<T>() {
        override fun onInactive() = func()
    }

    val result = MediatorLiveData<T>()
    result.addSource(this) { value -> result.value = value }
    result.addSource(hook) { }
    return result
}
//endregion
