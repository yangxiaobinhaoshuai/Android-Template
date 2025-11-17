package com.wkj.rv.lib.common

import com.wkj.rv.lib.RvVh

fun interface ExecutablePayload<T : RvVh> {
    fun onBindViewHolder(holder: T, position: Int)
}

typealias BindingParam = Pair<SmartViewHolder, Int>

fun interface SmartPayload : ExecutablePayload<SmartViewHolder> {

    fun onBindViewHolder(bind: BindingParam)

    override fun onBindViewHolder(
        holder: SmartViewHolder,
        position: Int
    ) {
        onBindViewHolder(holder to position)
    }
}