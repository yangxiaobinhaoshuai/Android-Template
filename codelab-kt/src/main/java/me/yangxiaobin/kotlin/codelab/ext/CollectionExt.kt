package me.yangxiaobin.kotlin.codelab.ext

import java.util.*


inline fun <K,V> Enumeration<K>.mappedTo(mapping: (K) -> V): Enumeration<V> {

    val list = mutableListOf<V>()
    for (k: K in this) {
        list.add(mapping(k))
    }

    return object : Enumeration<V> {

        val iterator = list.iterator()

        override fun hasMoreElements(): Boolean = iterator.hasNext()

        override fun nextElement(): V = iterator.next()

    }
}

fun <T> Collection<T>.replace(newElement: T, condition: (T) -> Boolean): Collection<T> = this.map { e -> if (condition.invoke(e)) newElement else e }


