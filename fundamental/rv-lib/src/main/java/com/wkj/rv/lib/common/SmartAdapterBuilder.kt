package com.wkj.rv.lib.common

import androidx.annotation.LayoutRes

class SmartAdapterBuilder {

    val delegates = mutableListOf<ItemDelegate<out Any>>()

    inline fun <reified T : Any> register(
        @LayoutRes layoutId: Int,
        noinline block: ItemDelegateBuilder<T>.() -> Unit
    ) {
        val b = ItemDelegateBuilder<T>(layoutId)
        b.block()
        delegates += b.build(T::class)
    }
}

fun smartAdapter(
    block: SmartAdapterBuilder.() -> Unit
): SmartAdapter {
    val builder = SmartAdapterBuilder()
    builder.block()
    return SmartAdapter(builder.delegates)
}
