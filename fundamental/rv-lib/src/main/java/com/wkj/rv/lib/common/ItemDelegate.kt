package com.wkj.rv.lib.common

import androidx.annotation.LayoutRes
import com.wkj.rv.lib.uitls.BiTypedParam
import com.wkj.rv.lib.uitls.ClickParam
import kotlin.reflect.KClass

data class ItemDelegate<T : Any>(
    val clazz: KClass<T>,
    @get:LayoutRes val layoutId: Int,
    val areItemsSame: (BiTypedParam<T>) -> Boolean,
    val areContentsSame: (BiTypedParam<T>) -> Boolean,
    val getChangePayload: (BiTypedParam<T>) -> Any?,
    val onBind: SmartViewHolder.(item: T) -> Unit,
    val onClick: ((p: ClickParam<T>) -> Unit)?,
    val onLongClick: ((p: ClickParam<T>) -> Boolean)?,
)
