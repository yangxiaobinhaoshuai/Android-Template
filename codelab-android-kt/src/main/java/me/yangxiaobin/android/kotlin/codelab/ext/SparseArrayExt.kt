package me.yangxiaobin.android.kotlin.codelab.ext

import android.util.SparseArray


/**
 * @see androidx.core.util
 * @see androidx.core.util.forEach
 * SparseArrayExt.kt
 */


fun <T> SparseArray<T>.toList(): List<T> {

    val list = mutableListOf<T>()

    for (index in 0 until this.size()) {
        val value = this.valueAt(index)
        list += value
    }

    return list
}
