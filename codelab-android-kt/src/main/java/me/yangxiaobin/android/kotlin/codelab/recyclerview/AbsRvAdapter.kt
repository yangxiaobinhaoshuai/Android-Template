package me.yangxiaobin.android.kotlin.codelab.recyclerview

import android.util.SparseArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.IdRes
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch


typealias RvVh = androidx.recyclerview.widget.RecyclerView.ViewHolder
typealias RvAdapter <VH> = androidx.recyclerview.widget.RecyclerView.Adapter<VH>

open class AbsVh<T>(
    itemView: View,
    private val bindingWithPayloads: suspend AbsVh<T>.(T, List<Any>) -> Unit = { _, _ -> },
    private val binding: suspend AbsVh<T>.(T) -> Unit = {},
) : RecyclerView.ViewHolder(itemView) {
    suspend fun bindTo(data: T) = binding.invoke(this, data)

    suspend fun bindToWithPayloads(data: T, payloads: List<Any> = emptyList()) =
        bindingWithPayloads.invoke(this, data, payloads)

    private val viewCache = SparseArray<View>(16)
    @Suppress("UNCHECKED_CAST")
    open fun <T : View?> findView(@IdRes resId: Int): T? = (viewCache[resId] as? T)
        ?: itemView.findViewById<T>(resId)?.also { viewCache.put(resId, it) }

    open fun <T : View> requireView(@IdRes resId: Int): T = this.findView<T>(resId)
        ?: throw NullPointerException("Can NOT find view relative to id: $this")
}

typealias VHBuilder<T> = (parent: View, viewType: Int) -> AbsVh<T>

fun <T> layoutResVhBuilder(layoutResId: Int, binding: suspend AbsVh<T>.(T) -> Unit): VHBuilder<T> =
    { parent: View, _: Int ->
        LayoutInflater
            .from(parent.context)
            .inflate(layoutResId, parent as ViewGroup, false)
            .let { AbsVh(itemView = it, binding = binding) }
    }

fun <T> layoutResVhBuilderWithPayloads(
    layoutResId: Int,
    bindingWithPayloads: suspend AbsVh<T>.(T, List<Any>) -> Unit = { _, _ -> },
): VHBuilder<T> =
    { parent: View, _: Int ->
        LayoutInflater
            .from(parent.context)
            .inflate(layoutResId, parent as ViewGroup, false)
            .let { AbsVh(itemView = it, bindingWithPayloads = bindingWithPayloads) }
    }


open class AbsRvAdapter<T, VH : AbsVh<T>>(
    private val dataList: List<T>,
    private val vhBuilder: VHBuilder<T>,
    private val scope: CoroutineScope,
) : RvAdapter<VH>() {

    override fun onBindViewHolder(holder: VH, position: Int) {
        scope.launch { holder.bindTo(dataList[position]) }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        @Suppress("UNCHECKED_CAST")
        return vhBuilder.invoke(parent, viewType) as VH
    }

    override fun getItemCount(): Int = dataList.size
}


open class AbsRvListAdapter<T, VH : AbsVh<T>>(
    private val scope: CoroutineScope,
    diffCallback: DiffUtil.ItemCallback<T>,
    private val vhBuilder: VHBuilder<T>,
) : androidx.recyclerview.widget.ListAdapter<T, VH>(diffCallback) {

    override fun onBindViewHolder(holder: VH, position: Int, payloads: MutableList<Any>) {
        super.onBindViewHolder(holder, position, payloads)
        val dataList: MutableList<T> = currentList
        scope.launch { holder.bindToWithPayloads(dataList[position], payloads) }
    }


    override fun onBindViewHolder(holder: VH, position: Int) {
        /*val dataList: MutableList<T> = currentList
        scope.launch { holder.bindToWithPayloads(dataList[position]) }*/
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        @Suppress("UNCHECKED_CAST")
        return vhBuilder.invoke(parent, viewType) as VH
    }

}
