package com.wkj.rv.lib.common

import android.util.SparseArray
import android.view.View
import androidx.annotation.IdRes
import androidx.recyclerview.widget.RecyclerView

class SmartViewHolder(
    val root: View
) : RecyclerView.ViewHolder(root) {

    private val cache = SparseArray<View>()

    @Suppress("UNCHECKED_CAST")
    fun <V : View> view(@IdRes id: Int): V {
        var v = cache.get(id)
        if (v == null) {
            v = root.findViewById(id)
            requireNotNull(v) { "No view with id=$id in ${root.id}" }
            cache.put(id, v)
        }
        return v as V
    }
}
