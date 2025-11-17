package com.wkj.rv.lib.common

import androidx.recyclerview.widget.DiffUtil
import kotlin.reflect.KClass

class SmartDiffCallback(
    delegates: List<ItemDelegate<out Any>>,
) : DiffUtil.ItemCallback<Any>() {

    private val delegateMap: Map<KClass<*>, ItemDelegate<out Any>> =
        delegates.associateBy { it.clazz }

    @Throws(IllegalStateException::class)
    private fun delegateOf(item: Any): ItemDelegate<out Any> =
        delegateMap[item::class] ?: error("No delegate registered for ${item::class.qualifiedName}")

    @Suppress("UNCHECKED_CAST")
    override fun areItemsTheSame(oldItem: Any, newItem: Any): Boolean {
        if (oldItem::class != newItem::class) return false
        val d = delegateOf(oldItem) as ItemDelegate<Any>
        return d.areItemsSame(oldItem to newItem)
    }

    @Suppress("UNCHECKED_CAST")
    override fun areContentsTheSame(oldItem: Any, newItem: Any): Boolean {
        if (oldItem::class != newItem::class) return false
        val d = delegateOf(oldItem) as ItemDelegate<Any>
        return d.areContentsSame(oldItem to newItem)
    }

    @Suppress("UNCHECKED_CAST")
    override fun getChangePayload(oldItem: Any, newItem: Any): Any? {
        if (oldItem::class != newItem::class) return null
        val d = delegateOf(oldItem) as ItemDelegate<Any>
        return d.getChangePayload(oldItem to newItem)
    }
}
