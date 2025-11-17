package com.wkj.rv.lib.common

import android.util.SparseArray
import android.view.View
import androidx.annotation.IdRes
import androidx.recyclerview.widget.RecyclerView

class SmartViewHolder(
    private val root: View,
) : RecyclerView.ViewHolder(root) {

    private val viewCache = SparseArray<View>()

    fun <T : View> requireView(@IdRes id: Int): T =
        get(id) ?: throw NullPointerException("Can NOT find view relative to id: $id")

    @Throws(ClassCastException::class)
    @Suppress("UNCHECKED_CAST")
    operator fun <V : View> get(@IdRes id: Int): V? = (viewCache[id] as? V)
        ?: root.findViewById<V>(id)?.also { viewCache.put(id, it) }
}
