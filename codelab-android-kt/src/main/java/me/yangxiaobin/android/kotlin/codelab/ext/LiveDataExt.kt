package me.yangxiaobin.android.kotlin.codelab.ext

import androidx.annotation.CheckResult
import androidx.annotation.IntRange
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData


@CheckResult
fun <T> LiveData<T>.toMutable(): MutableLiveData<T> {

    if (this is MediatorLiveData) return this

    val mediator = MediatorLiveData<T>()
    mediator.addSource(this) { data: T ->
        mediator.value = data
    }
    return mediator
}

@CheckResult
fun <T> LiveData<T>.onChanged(onChanged: (T) -> Unit): LiveData<T> {

    val mediator = MediatorLiveData<T>()

    this.observeForever { data: T ->
        mediator.postValue(data)
    }

    return mediator
}

/**
 * 只监听获取LiveData的第一个值
 */
@CheckResult
fun <T> LiveData<T>.first(): LiveData<T> = take(1)


/**
 * 只监听获取LiveData的 [count] 个值
 */
@CheckResult
fun <T> LiveData<T>.take(@IntRange(from = 1, to = Long.MAX_VALUE) count: Int): LiveData<T> {
    require(count > 0) { "count must be greater than 1" }
    var counter = 0
    return takeWhile { counter++ < count }
}

/**
 * 只监听获取LiveData的 [count] 个值
 */
@CheckResult
fun <T> LiveData<T>.drop(@IntRange(from = 1, to = Long.MAX_VALUE) count: Int): LiveData<T> {
    require(count > 0) { "count must be greater than 1" }
    var counter = 0
    return takeWhile { counter++ < count }
}


/**
 * 监听LiveData的值更新，直到 [predicate] 返回false
 */
@CheckResult
inline fun <T> LiveData<T>.takeWhile(crossinline predicate: (T) -> Boolean): LiveData<T> {
    val result = MediatorLiveData<T>()
    result.addSource(this) { value ->
        if (predicate(value as T)) result.value = value
        else result.removeSource(this)
    }
    return result
}

@CheckResult
fun <T> LiveData<T>.skip(count: Int): LiveData<T> {
    var counter = 0
    return skipWhile { ++counter > count }
}

@CheckResult
inline fun <T> LiveData<T>.skipWhile(crossinline predicate: (T) -> Boolean): LiveData<T> {
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


//过滤相关
@CheckResult
inline fun <T> LiveData<T>.filter(crossinline func: (T) -> Boolean): LiveData<T> {
    val result = MediatorLiveData<T>()
    result.addSource(this) { value ->
        if (func(value)) {
            result.value = value
        }
    }
    return result
}

/**
 * LiveData 触发 [LiveData.onActive] 回调
 */
@CheckResult
inline fun <T> LiveData<T>.doOnActive(crossinline func: () -> Unit): LiveData<T> {
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
inline fun <T> LiveData<T>.doOnInactive(crossinline func: () -> Unit): LiveData<T> {
    val hook = object : LiveData<T>() {
        override fun onInactive() = func()
    }

    val result = MediatorLiveData<T>()
    result.addSource(this) { value -> result.value = value }
    result.addSource(hook) { }
    return result
}
