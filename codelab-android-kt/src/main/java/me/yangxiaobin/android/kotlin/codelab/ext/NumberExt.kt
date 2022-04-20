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

fun Number.toFixed(fractionDigits: Int): String {
    return try {
        "%.${fractionDigits}f".format(this)
    } catch (e: Exception) {
        e.printStackTrace()
        this.toString()
    }
}


val Int.isPosition get() = this > 0
val Int.isNonPosition get() = this <= 0
val Int.isNegative get() = this < 0
val Int.isNonNegative get() = this >= 0
