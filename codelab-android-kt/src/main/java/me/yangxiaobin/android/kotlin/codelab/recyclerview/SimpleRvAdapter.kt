package me.yangxiaobin.android.kotlin.codelab.recyclerview

import android.util.SparseArray
import android.view.View
import android.view.ViewGroup
import androidx.annotation.IdRes
import androidx.recyclerview.widget.RecyclerView
import me.yangxiaobin.android.kotlin.codelab.ext.context.inflater
import me.yangxiaobin.android.kotlin.codelab.log.L
import me.yangxiaobin.logger.core.LogLevel
import me.yangxiaobin.logger.log


open class SimpleVH(wholeItemView: View) : RecyclerView.ViewHolder(wholeItemView) {

    private val viewCache = SparseArray<View>(16)

    private val tag = "AbsVH"
    private val logI by lazy { L.log(LogLevel.INFO, tag) }

    @Suppress("UNCHECKED_CAST")
    open fun <T : View?> findView(@IdRes resId: Int): T? = (viewCache[resId] as? T)
        ?: itemView.findViewById<T>(resId)?.also { viewCache.put(resId, it) }
        ?: run { logI("Can not find associated view with id: $resId");null }

    open fun <T : View> requireView(@IdRes resId: Int): T = this.findView<T>(resId)
        ?: throw NullPointerException("Can NOT find view relative to id: $this")
}

/**
 * T for ViewHolder
 */
typealias VhBinding <VH, T> = (VhTuple<VH, T>) -> Unit

data class VhTuple<VH, T>(val vh: VH, val entity: T?, val pos: Int, val payloads: MutableList<Any>)

class SimpleRvAdapter<T>(
    private val dataList: List<T>,
    private val itemLayoutResId: Int,
    private val binding: VhBinding<SimpleVH, T>,
) : RecyclerView.Adapter<SimpleVH>() {

    @Suppress("PrivatePropertyName")
    private val TAG get() = "SimpleRvAdapter"
    private val logI by lazy { L.log(LogLevel.INFO, TAG) }

    override fun getItemCount(): Int = dataList.size

    override fun getItemViewType(position: Int): Int {
        return super.getItemViewType(position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SimpleVH {
        val vhItemView = parent.context.inflater.inflate(itemLayoutResId, parent, false)
        logI("onCreateViewHolder")
        return SimpleVH(vhItemView)
    }

    override fun onBindViewHolder(holder: SimpleVH, position: Int, payloads: MutableList<Any>) {
        logI("onBindViewHolder with payloads")

        if (payloads.isNotEmpty())
            binding.invoke(
                VhTuple(holder, dataList.getOrNull(position), position, payloads)
            )
        else super.onBindViewHolder(holder, position, payloads)

    }

    override fun onBindViewHolder(holder: SimpleVH, position: Int) {
        logI("onBindViewHolder")
        binding.invoke(
            VhTuple(holder, dataList.getOrNull(position), position, mutableListOf())
        )
    }

}
