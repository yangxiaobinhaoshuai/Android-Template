package me.yangxiaobin.android.kotlin.codelab.base.ability

import android.content.res.Resources
import android.util.TypedValue

interface DimensionAbility {
    val Int.dpf: Float get() = this * Resources.getSystem().displayMetrics.density
    val Int.dp: Int get() = this.dpf.toInt()

    val Int.spf: Float
        get() = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_SP,
            this.toFloat(), // this 指向 Int 值，需要转为 Float
            Resources.getSystem().displayMetrics
        )
    val Int.sp: Int get() = (this * Resources.getSystem().displayMetrics.scaledDensity).toInt()

}