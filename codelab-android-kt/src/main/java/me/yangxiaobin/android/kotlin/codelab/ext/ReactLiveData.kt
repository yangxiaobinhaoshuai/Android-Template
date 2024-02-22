package me.yangxiaobin.android.kotlin.codelab.ext

import androidx.annotation.IntRange
import androidx.annotation.MainThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map

object ReactiveLiveData {

    private val NOT_SET = Any()

    @MainThread
    @Suppress("UNCHECKED_CAST")
    fun <T, R> combineLatest(
        sources: Array<out LiveData<out T>>,
        combiner: (Array<T>) -> R
    ): LiveData<R> {
        if (sources.isEmpty()) return MutableLiveData()

        val size = sources.size
        val result = MediatorLiveData<Any>()

        val values = arrayOfNulls<Any?>(size)
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

                if (combine) {
                    result.value = combiner(values as Array<T>)
                }
            }
            result.addSource(sources[index] as LiveData<Any>, observer)
        }
        return result as LiveData<R>
    }

    @MainThread
    @JvmStatic
    fun <T, R> combineLatest(combiner: (Array<T>) -> R, vararg sources: LiveData<T>): LiveData<R> {
        return combineLatest(sources, combiner)
    }

    @MainThread
    @JvmStatic
    @Suppress("UNCHECKED_CAST")
    fun <T1, T2, R> combineLatest(
        source1: LiveData<T1>,
        source2: LiveData<T2>,
        combiner: (T1, T2) -> R
    ): LiveData<R> {
        val func = { input: Array<Any> -> combiner(input[0] as T1, input[1] as T2) }

        return combineLatest(func, source1 as LiveData<Any>, source2 as LiveData<Any>)
    }

    @MainThread
    @JvmStatic
    fun <T1, T2> combineLatest(
        source1: LiveData<T1>,
        source2: LiveData<T2>
    ): LiveData<Pair<T1, T2>> {
        return combineLatest(source1, source2) { t1, t2 -> t1 to t2 }
    }

    @MainThread
    @JvmStatic
    @Suppress("UNCHECKED_CAST")
    fun <T1, T2, T3, R> combineLatest(
        source1: LiveData<T1>,
        source2: LiveData<T2>,
        source3: LiveData<T3>,
        combiner: (T1, T2, T3) -> R
    ): LiveData<R> {
        val func = { input: Array<Any> ->
            combiner(input[0] as T1, input[1] as T2, input[2] as T3)
        }
        return combineLatest(
            func,
            source1 as LiveData<Any>,
            source2 as LiveData<Any>,
            source3 as LiveData<Any>
        )
    }

    @MainThread
    @JvmStatic
    @Suppress("UNCHECKED_CAST")
    fun <T1, T2, T3, T4, R> combineLatest(
        source1: LiveData<T1>,
        source2: LiveData<T2>,
        source3: LiveData<T3>,
        source4: LiveData<T4>,
        combiner: (T1, T2, T3, T4) -> R
    ): LiveData<R> {
        val func = { input: Array<Any> ->
            combiner(input[0] as T1, input[1] as T2, input[2] as T3, input[3] as T4)
        }
        return combineLatest(
            func,
            source1 as LiveData<Any>,
            source2 as LiveData<Any>,
            source3 as LiveData<Any>,
            source4 as LiveData<Any>
        )
    }

    @MainThread
    @JvmStatic
    @Suppress("UNCHECKED_CAST")
    fun <T1, T2, T3, T4, T5, R> combineLatest(
        source1: LiveData<T1>,
        source2: LiveData<T2>,
        source3: LiveData<T3>,
        source4: LiveData<T4>,
        source5: LiveData<T5>,
        combiner: (T1, T2, T3, T4, T5) -> R
    ): LiveData<R> {
        val func = { input: Array<Any> ->
            combiner(input[0] as T1, input[1] as T2, input[2] as T3, input[3] as T4, input[4] as T5)
        }
        return combineLatest(
            func,
            source1 as LiveData<Any>,
            source2 as LiveData<Any>,
            source3 as LiveData<Any>,
            source4 as LiveData<Any>,
            source5 as LiveData<Any>
        )
    }

    @MainThread
    @JvmStatic
    @Suppress("UNCHECKED_CAST")
    fun <T1, T2, T3, T4, T5, T6, R> combineLatest(
        source1: LiveData<T1>,
        source2: LiveData<T2>,
        source3: LiveData<T3>,
        source4: LiveData<T4>,
        source5: LiveData<T5>,
        source6: LiveData<T6>,
        combiner: (T1, T2, T3, T4, T5, T6) -> R
    ): LiveData<R> {
        val func = { input: Array<Any> ->
            combiner(
                input[0] as T1,
                input[1] as T2,
                input[2] as T3,
                input[3] as T4,
                input[4] as T5,
                input[5] as T6
            )
        }
        return combineLatest(
            func,
            source1 as LiveData<Any>,
            source2 as LiveData<Any>,
            source3 as LiveData<Any>,
            source4 as LiveData<Any>,
            source5 as LiveData<Any>,
            source6 as LiveData<Any>
        )
    }

    @MainThread
    @JvmStatic
    @Suppress("UNCHECKED_CAST")
    fun <T1, T2, T3, T4, T5, T6, T7, R> combineLatest(
        source1: LiveData<T1>,
        source2: LiveData<T2>,
        source3: LiveData<T3>,
        source4: LiveData<T4>,
        source5: LiveData<T5>,
        source6: LiveData<T6>,
        source7: LiveData<T7>,
        combiner: (T1, T2, T3, T4, T5, T6, T7) -> R
    ): LiveData<R> {
        val func = { input: Array<Any> ->
            combiner(
                input[0] as T1,
                input[1] as T2,
                input[2] as T3,
                input[3] as T4,
                input[4] as T5,
                input[5] as T6,
                input[6] as T7
            )
        }
        return combineLatest(
            func,
            source1 as LiveData<Any>,
            source2 as LiveData<Any>,
            source3 as LiveData<Any>,
            source4 as LiveData<Any>,
            source5 as LiveData<Any>,
            source6 as LiveData<Any>,
            source7 as LiveData<Any>
        )
    }

    @MainThread
    @JvmStatic
    @Suppress("UNCHECKED_CAST")
    fun <T1, T2, T3, T4, T5, T6, T7, T8, R> combineLatest(
        source1: LiveData<T1>,
        source2: LiveData<T2>,
        source3: LiveData<T3>,
        source4: LiveData<T4>,
        source5: LiveData<T5>,
        source6: LiveData<T6>,
        source7: LiveData<T7>,
        source8: LiveData<T8>,
        combiner: (T1, T2, T3, T4, T5, T6, T7, T8) -> R
    ): LiveData<R> {
        val func = { input: Array<Any> ->
            combiner(
                input[0] as T1,
                input[1] as T2,
                input[2] as T3,
                input[3] as T4,
                input[4] as T5,
                input[5] as T6,
                input[6] as T7,
                input[7] as T8
            )
        }
        return combineLatest(
            func,
            source1 as LiveData<Any>,
            source2 as LiveData<Any>,
            source3 as LiveData<Any>,
            source4 as LiveData<Any>,
            source5 as LiveData<Any>,
            source6 as LiveData<Any>,
            source7 as LiveData<Any>,
            source8 as LiveData<Any>
        )
    }

    @MainThread
    @JvmStatic
    @Suppress("UNCHECKED_CAST")
    fun <T1, T2, T3, T4, T5, T6, T7, T8, T9, R> combineLatest(
        source1: LiveData<T1>,
        source2: LiveData<T2>,
        source3: LiveData<T3>,
        source4: LiveData<T4>,
        source5: LiveData<T5>,
        source6: LiveData<T6>,
        source7: LiveData<T7>,
        source8: LiveData<T8>,
        source9: LiveData<T9>,
        combiner: (T1, T2, T3, T4, T5, T6, T7, T8, T9) -> R
    ): LiveData<R> {
        val func = { input: Array<Any> ->
            combiner(
                input[0] as T1,
                input[1] as T2,
                input[2] as T3,
                input[3] as T4,
                input[4] as T5,
                input[5] as T6,
                input[6] as T7,
                input[7] as T8,
                input[8] as T9
            )
        }
        return combineLatest(
            func,
            source1 as LiveData<Any>,
            source2 as LiveData<Any>,
            source3 as LiveData<Any>,
            source4 as LiveData<Any>,
            source5 as LiveData<Any>,
            source6 as LiveData<Any>,
            source7 as LiveData<Any>,
            source8 as LiveData<Any>,
            source9 as LiveData<Any>
        )
    }

    @MainThread
    @JvmStatic
    @Suppress("UNCHECKED_CAST")
    fun <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, R> combineLatest(
        source1: LiveData<T1>,
        source2: LiveData<T2>,
        source3: LiveData<T3>,
        source4: LiveData<T4>,
        source5: LiveData<T5>,
        source6: LiveData<T6>,
        source7: LiveData<T7>,
        source8: LiveData<T8>,
        source9: LiveData<T9>,
        source10: LiveData<T10>,
        combiner: (T1, T2, T3, T4, T5, T6, T7, T8, T9, T10) -> R
    ): LiveData<R> {
        val func = { input: Array<Any> ->
            combiner(
                input[0] as T1,
                input[1] as T2,
                input[2] as T3,
                input[3] as T4,
                input[4] as T5,
                input[5] as T6,
                input[6] as T7,
                input[7] as T8,
                input[8] as T9,
                input[9] as T10
            )
        }
        return combineLatest(
            func,
            source1 as LiveData<Any>,
            source2 as LiveData<Any>,
            source3 as LiveData<Any>,
            source4 as LiveData<Any>,
            source5 as LiveData<Any>,
            source6 as LiveData<Any>,
            source7 as LiveData<Any>,
            source8 as LiveData<Any>,
            source9 as LiveData<Any>,
            source10 as LiveData<Any>
        )
    }

    @MainThread
    @JvmStatic
    fun <T1, T2, T3> combineLatest(
        source1: LiveData<T1>,
        source2: LiveData<T2>,
        source3: LiveData<T3>
    ): LiveData<Triple<T1, T2, T3>> {
        return combineLatest(source1, source2, source3) { t1, t2, t3 -> Triple(t1, t2, t3) }
    }

    @MainThread
    @JvmStatic
    fun <T> merge(vararg sources: LiveData<T>): LiveData<T> {
        val result = MediatorLiveData<T>()

        for (source in sources) {
            result.addSource(source, result::setValue)
        }
        return result
    }
}

//fun <T> LiveData<T>.toMutable():MutableLiveData<T> = MediatorLiveData<T>().apply { this.addSource(this@toMutable,this::setValue) }

fun <T> LiveData<T>.onEach(action: (T) -> Unit): LiveData<T> = this.map { action.invoke(it);it }

fun <T> T.toLiveData(): LiveData<T> = MutableLiveData(this)

// region buffer
//@Suppress("UNCHECKED_CAST")
//fun <T> LiveData<T>.buffer(count: Int, skip: Int): LiveData<List<T>> {
//    val buffer: Array<Any?> = arrayOfNulls(if (count > skip) skip else count)
//    val result = MediatorLiveData<List<T>>()
//    var counter = 0
//    result.addSource(this) {
//        if (counter < count) {
//            buffer[counter] = it
//        }
//        counter++
//        if (counter == skip) {
//            counter = 0
//            result.value = buffer.toList() as List<T>
//        }
//    }
//    return result
//}

//fun <T> LiveData<T>.buffer(count: Int): LiveData<List<T>> =
//    buffer(count, count)
// endregion



// region startWith
fun <T> LiveData<T>.startWith(value: T): LiveData<T> =
    startWith(MutableLiveData(value))

fun <T> LiveData<T>.startWith(value: () -> T): LiveData<T> =
    startWith(MutableLiveData(value()))

fun <T> LiveData<T>.startWith(value: LiveData<T>, dropStart: Boolean = true): LiveData<T> {
    val result = MediatorLiveData<T>()
    result.addSource(value) { valueStart ->
        if (dropStart) {
            result.removeSource(value)
        }
        result.value = valueStart
        result.addSource(this) { valueMain ->
            result.value = valueMain
        }
    }
    return result
}
// endregion



// region first & take & skip
/**
 * 只监听获取LiveData的第一个值
 */
//fun <T> LiveData<T>.first(): LiveData<T> =
//    take(1)

/**
 * 只监听获取LiveData的 [count] 个值
 */
//fun <T> LiveData<T>.take(@IntRange(from = 1, to = Long.MAX_VALUE) count: Int): LiveData<T> {
//    require(count > 0) { "count must be greater than 1" }
//    var counter = 0
//    return takeWhile { counter++ < count }
//}

/**
 * 监听LiveData的值更新，直到 [predicate] 返回false
 */
//inline fun <T> LiveData<T>.takeWhile(crossinline predicate: (T) -> Boolean): LiveData<T> {
//    val result = MediatorLiveData<T>()
//    result.addSource(this) { value ->
//        if (predicate(value as T)) result.value = value
//        else result.removeSource(this)
//    }
//    return result
//}


//fun <T> LiveData<T>.skip(count: Int): LiveData<T> {
//    var counter = 0
//    return skipWhile { ++counter > count }
//}

//inline fun <T> LiveData<T>.skipWhile(crossinline predicate: (T) -> Boolean): LiveData<T> {
//    val result = MediatorLiveData<T>()
//    var drop = true
//    result.addSource(this) { value ->
//        if (!drop || predicate(value)) {
//            drop = false
//            result.value = value
//        }
//    }
//    return result
//}
// endregion



// region filter 过滤相关
//inline fun <T> LiveData<T>.filter(crossinline func: (T) -> Boolean): LiveData<T> {
//    val result = MediatorLiveData<T>()
//    result.addSource(this) { value ->
//        if (func(value)) {
//            result.value = value
//        }
//    }
//    return result
//}

//inline fun <T> LiveData<T>.filterNot(crossinline func: (T) -> Boolean): LiveData<T> =
//    filter { value -> !func(value) }

//@Suppress("UNCHECKED_CAST")
//fun <T> LiveData<T?>.filterNotNull(): LiveData<T> =
//    filter { it != null } as LiveData<T>

//@Suppress("UNCHECKED_CAST")
//inline fun <reified T> LiveData<Any>.filterIsInstance(): LiveData<T> =
//    filter { it is T } as LiveData<T>
// endregion



// region distinct
//fun <T> LiveData<T>.distinct(): LiveData<T> =
//    distinct { value -> value }

//inline fun <T, K> LiveData<T>.distinct(crossinline func: (T) -> K): LiveData<T> {
//    val keys = hashSetOf<K>()
//    return filter { keys.add(func(it)) }
//}

//inline fun <T, K> LiveData<T>.distinctUntilChanged(crossinline func: (T) -> K): LiveData<T> {
//    var prev: Any? = null
//    return filter {
//        val key = func(it)
//        if (key !== prev) {
//            prev = key
//            true
//        } else {
//            false
//        }
//    }
//}
// endregion



// region merge 合并 LiveData，任意的值更新都会触发
fun <T> Array<LiveData<T>>.merge(): LiveData<T> =
    ReactiveLiveData.merge(*this)

fun <T> Collection<LiveData<T>>.merge(): LiveData<T> =
    ReactiveLiveData.merge(*this.toTypedArray())

/**
 * merge 操作
 */
//infix operator fun <T> LiveData<T>.plus(other: LiveData<T>): LiveData<T> =
//    mergeWith(other)

fun <T> LiveData<T>.mergeWith(other: LiveData<T>): LiveData<T> =
    ReactiveLiveData.merge(this, other)

fun <T> LiveData<T>.mergeWith(other: Collection<LiveData<T>>): LiveData<T> =
    ReactiveLiveData.merge(this, *other.toTypedArray())

fun <T> LiveData<T>.mergeWith(other: Array<LiveData<T>>): LiveData<T> =
    ReactiveLiveData.merge(this, *other)
// endregion



//region combineLatest 组合 LiveData 所有的最新值，如果有LiveData没有更新值，则不触发
fun <T, R> Array<LiveData<T>>.combineLatest(combiner: (Array<T>) -> R): LiveData<R> =
    ReactiveLiveData.combineLatest(this, combiner)

fun <T, R> Collection<LiveData<out T>>.combineLatest(combiner: (Array<T>) -> R): LiveData<R> =
    ReactiveLiveData.combineLatest(this.toTypedArray(), combiner)

//fun <T1, T2, R> LiveData<T1>.combineLatestWith(
//    other: LiveData<T2>,
//    combiner: (T1, T2) -> R
//): LiveData<R> =
//    ReactiveLiveData.combineLatest(this, other, combiner)

fun <T1, T2> LiveData<T1>.combineLatestWith(
    other: LiveData<T2>)
    : LiveData<Pair<T1, T2>> =
    ReactiveLiveData.combineLatest(this, other) { t1, t2 -> t1 to t2 }
// endregion



// region Active 状态监听
/**
 * LiveData 触发 [LiveData.onActive] 回调
 */
//inline fun <T> LiveData<T>.doOnActive(crossinline func: () -> Unit): LiveData<T> {
//    val hook = object : LiveData<T>() {
//        override fun onActive() = func()
//    }
//
//    val result = MediatorLiveData<T>()
//    result.addSource(this) { result.value = it }
//    result.addSource(hook) { }
//    return result
//}

/**
 * LiveData 触发 [LiveData.onInactive] 回调
 */
//inline fun <T> LiveData<T>.doOnInactive(crossinline func: () -> Unit): LiveData<T> {
//    val hook = object : LiveData<T>() {
//        override fun onInactive() = func()
//    }
//
//    val result = MediatorLiveData<T>()
//    result.addSource(this) { value -> result.value = value }
//    result.addSource(hook) { }
//    return result
//}
// endregion
