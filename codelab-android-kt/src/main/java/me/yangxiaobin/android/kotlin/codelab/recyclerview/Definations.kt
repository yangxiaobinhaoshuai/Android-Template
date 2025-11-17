package me.yangxiaobin.android.kotlin.codelab.recyclerview

import android.util.SparseArray
import android.view.View
import android.view.ViewGroup
import androidx.annotation.IdRes
import androidx.recyclerview.widget.RecyclerView


typealias RvVh = RecyclerView.ViewHolder
typealias RvAdapter <VH> = RecyclerView.Adapter<VH>


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
        get<View>(id) ?: throw NullPointerException("Can NOT find view relative to id: $id")

    @Throws(ClassCastException::class)
    @Suppress("UNCHECKED_CAST")
    operator fun <V : View> get(@IdRes id: Int): V? = (viewCache[id] as? V)
        ?: rootView.findViewById<V>(id)?.also { viewCache.put(id, it) }

    fun <ENTITY> onBind()

    private companion object KEY {
        private val KEY_CACHE = View.generateViewId()   // 或 R.id.view_cache_id
    }
}


/**
 * @see RecyclerView.Adapter
 */
interface RvAdapterAlike<T : RvVh> {

    fun onCreateViewHolder(parent: ViewGroup, viewType: Int): T

    fun getItemCount(): Int

    fun onBindViewHolder(holder: T, position: Int)
}