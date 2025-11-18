package com.wkj.rv.lib.common

import android.util.SparseArray
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import kotlin.reflect.KClass

class SmartAdapter(
    delegates: List<ItemDelegate<out Any>>,
) : ListAdapter<Any, SmartViewHolder>(
    SmartDiffCallback(delegates)
) {

    // kclass -> delegate
    private val delegateMap: Map<KClass<*>, ItemDelegate<out Any>> =
        delegates.associateBy { it.clazz }

    // viewType -> delegate
    private val viewTypeMap: SparseArray<ItemDelegate<out Any>> =
        SparseArray<ItemDelegate<out Any>>().apply {
            delegates.forEachIndexed { index, delegate ->
                put(index, delegate)
            }
        }

    override fun getItemViewType(position: Int): Int {
        val item = getItem(position)
        val k = item::class
        val idx = delegateMap.keys.indexOf(k)
        require(idx >= 0) { "No delegate registered for ${k.qualifiedName}" }
        return idx
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SmartViewHolder {
        val delegate = viewTypeMap[viewType]
            ?: error("No delegate for viewType=$viewType")
        val v = LayoutInflater.from(parent.context).inflate(delegate.layoutId, parent, false)
        val vh = SmartViewHolder(v)

        // 安装 click / longClick 逻辑（基于 delegate）
        v.setOnClickListener {
            val position = vh.bindingAdapterPosition
            if (position != RecyclerView.NO_POSITION) {
                val item = getItem(position) as Any
                @Suppress("UNCHECKED_CAST")
                (delegate as ItemDelegate<Any>).onClick?.invoke(item to position)
            }
        }

        v.setOnLongClickListener {
            val position = vh.bindingAdapterPosition
            if (position != RecyclerView.NO_POSITION) {
                val item = getItem(position) as Any

                @Suppress("UNCHECKED_CAST")
                val consumed = (delegate as ItemDelegate<Any>).onLongClick?.invoke(item to position)
                consumed ?: false
            } else false
        }

        // 子 View click
        @Suppress("UNCHECKED_CAST")
        (delegate as ItemDelegate<Any>).subClicks.forEach { (id, handler) ->
            // SmartViewHolder 已经有缓存方法
            val child = vh.get<android.view.View>(id)
            child?.setOnClickListener {
                val position = vh.bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val item = getItem(position) as Any
                    handler(item to position)
                }
            }
        }

        // 子 View longClick
        delegate.subLongClicks.forEach { (id, handler) ->
            val child = vh.get<android.view.View>(id)
            child?.setOnLongClickListener {
                val position = vh.bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val item = getItem(position) as Any
                    handler(item to position)
                } else false
            }
        }

        return vh
    }

    override fun onBindViewHolder(holder: SmartViewHolder, position: Int) {

        val item = getItem(position)
        val k = item::class
        val delegate: ItemDelegate<out Any> =
            delegateMap[k] ?: error("No delegate registered for ${k.qualifiedName}")

        @Suppress("UNCHECKED_CAST")
        ((delegate as ItemDelegate<Any>).onBind(holder, item))
    }

    @Throws(IllegalStateException::class)
    override fun onBindViewHolder(
        holder: SmartViewHolder,
        position: Int,
        payloads: MutableList<Any>,
    ) {

        if (payloads.isEmpty()) {
            onBindViewHolder(holder, position) // 正常全量绑定
            return
        }

        val smartPayloads = payloads.filterIsInstance<SmartPayload>()
        smartPayloads.forEach { it.onBindViewHolder(holder, position) }

        val remained = payloads - smartPayloads.toSet()
        // Throws an IllegalStateException if the value is false.
        check(remained.isEmpty()) {
            "Use SmartPayload instead of raw payload. remained=$remained"
        }

    }
}
