package com.wkj.rv.lib.common

import androidx.annotation.LayoutRes
import kotlin.reflect.KClass

data class ItemDelegate<T : Any>(
    val clazz: KClass<T>,
    @get:LayoutRes val layoutId: Int,
    val areItemsSame: (T, T) -> Boolean,
    val areContentsSame: (T, T) -> Boolean,
    val getChangePayload: (T, T) -> Any?,
    val onBind: SmartViewHolder.(item: T) -> Unit,
    val onClick: ((item: T, position: Int) -> Unit)?,
    val onLongClick: ((item: T, position: Int) -> Boolean)?,
)
