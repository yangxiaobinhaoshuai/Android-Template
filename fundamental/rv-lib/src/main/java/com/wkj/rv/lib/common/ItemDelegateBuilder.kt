package com.wkj.rv.lib.common

import androidx.annotation.LayoutRes
import kotlin.reflect.KClass

class ItemDelegateBuilder<T : Any>(
    @get:LayoutRes private val layoutId: Int,
) {
    var areItemsSame: (T, T) -> Boolean = { o, n -> o === n }
    var areContentsSame: (T, T) -> Boolean = { o, n -> o == n }
    var getChangePayload: (T, T) -> Any? = { _, _ -> null }

    private var _onClick: ((T, Int) -> Unit)? = null
    private var _onLongClick: ((T, Int) -> Boolean)? = null
    private var _onBind: SmartViewHolder.(T, List<Any>) -> Unit = { _, _ -> }

    fun onClick(block: (T, Int) -> Unit) {
        _onClick = block
    }

    fun onLongClick(block: (T, Int) -> Boolean) {
        _onLongClick = block
    }

    fun onBind(block: SmartViewHolder.(T, List<Any>) -> Unit) {
        _onBind = block
    }

    fun build(clazz: KClass<T>): ItemDelegate<T> =
        ItemDelegate(
            clazz = clazz,
            layoutId = layoutId,
            areItemsSame = areItemsSame,
            areContentsSame = areContentsSame,
            getChangePayload = getChangePayload,
            onBind = _onBind,
            onClick = _onClick,
            onLongClick = _onLongClick,
        )
}
