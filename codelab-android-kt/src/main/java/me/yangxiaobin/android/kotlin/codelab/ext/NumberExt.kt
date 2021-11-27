package me.yangxiaobin.android.kotlin.codelab.ext

import android.content.res.Resources
import android.util.TypedValue


val Number.dp2px: Float
    get() {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            this.toFloat(),
            Resources.getSystem().displayMetrics
        )
    }

