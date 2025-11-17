package com.wkj.rv.lib.dynamic

import android.util.SparseArray
import android.view.View
import android.view.ViewGroup
import androidx.annotation.IdRes
import androidx.recyclerview.widget.RecyclerView

/**
 * TODO ，暂无好的 idea
 * 一点不成熟的想法，想通过 retrofit style 实现 less boilerplate 的 RecyclerView.Adapter / ViewHolder 。
 */
/**
 * @see RecyclerView.ViewHolder
 * @see retrofit2.Retrofit.create
 */
interface RvVhAlike {
    val rootView: View
    private val viewCache: SparseArray<View>
        get() {
            @Suppress("UNCHECKED_CAST")
            var cache = rootView.getTag(KEY_CACHE) as? SparseArray<View>
            if (cache == null) {
                cache = SparseArray()
                rootView.setTag(KEY_CACHE, cache)
            }
            return cache
        }

    fun requireView(@IdRes id: Int): View =
        get(id) ?: throw NullPointerException("Can NOT find view relative to id: $id")

    @Throws(ClassCastException::class)
    @Suppress("UNCHECKED_CAST")
    operator fun <V : View> get(@IdRes id: Int): V? = (viewCache[id] as? V)
        ?: rootView.findViewById<V>(id)?.also { viewCache.put(id, it) }

    fun onBind()

    private companion object KEY {
        private val KEY_CACHE = View.generateViewId()   // 或 R.id.view_cache_id
    }
}


/**
 * @see RecyclerView.Adapter
 */
interface RvAdapterAlike<T : RvVhAlike> {
    fun onBindViewHolder(holder: T, position: Int)
    fun onBindViewHolder(holder: T, position: Int, payloads: List<Any?>)
    fun getItemCount(): Int
    fun onCreateViewHolder(parent: ViewGroup, viewType: Int): T
    fun getItemViewType(position: Int): Int
}