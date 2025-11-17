package com.wkj.rv.lib.common

import androidx.annotation.LayoutRes
import com.wkj.rv.lib.uitls.BiTypedParam
import com.wkj.rv.lib.uitls.ClickParam
import kotlin.reflect.KClass


class ItemDelegateBuilder<T : Any>(
    @field:LayoutRes private val layoutId: Int,
) {
    private var areItemsSame: (BiTypedParam<T>) -> Boolean = { (o, n) -> o === n }

    fun areItemsSame(block: (BiTypedParam<T>) -> Boolean) {
        areItemsSame = block
    }

    private var areContentsSame: (BiTypedParam<T>) -> Boolean = { (o, n) -> o == n }

    fun areContentsSame(block: (BiTypedParam<T>) -> Boolean) {
        areContentsSame = block
    }

    private var getChangePayload: (BiTypedParam<T>) -> SmartPayload? =
        { (_, _) -> SmartPayload.NOOP }

    fun getChangePayload(block: (BiTypedParam<T>) -> SmartPayload) {
        getChangePayload = block
    }

    private var _onClick: ((ClickParam<T>) -> Unit)? = null
    private var _onLongClick: ((ClickParam<T>) -> Boolean)? = null
    private var _onBind: SmartViewHolder.(T) -> Unit = { _ -> }

    fun onClick(block: (ClickParam<T>) -> Unit) {
        _onClick = block
    }

    fun onLongClick(block: (ClickParam<T>) -> Boolean) {
        _onLongClick = block
    }

    fun onBind(block: SmartViewHolder.(T) -> Unit) {
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
