// DiffCallbackAdapter.kt
package com.wkj.rv.lib

import androidx.recyclerview.widget.DiffUtil

class DiffCallbackAdapter<T : Any>(
    private val areItemsTheSame: (old: T, new: T) -> Boolean,
    private val areContentsTheSame: (old: T, new: T) -> Boolean = { o, n -> o == n },
    private val payloadProvider: ((old: T, new: T) -> Any?)? = null
) : DiffUtil.ItemCallback<T>() {

    override fun areItemsTheSame(oldItem: T, newItem: T): Boolean =
        areItemsTheSame.invoke(oldItem, newItem)

    override fun areContentsTheSame(oldItem: T, newItem: T): Boolean =
        areContentsTheSame.invoke(oldItem, newItem)

    override fun getChangePayload(oldItem: T, newItem: T): Any? =
        payloadProvider?.invoke(oldItem, newItem)
}