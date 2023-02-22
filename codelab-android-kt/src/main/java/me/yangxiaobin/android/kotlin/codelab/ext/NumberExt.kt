package me.yangxiaobin.android.kotlin.codelab.ext

import android.content.res.Resources
import android.util.TypedValue
import java.util.*


val Int.isPositive get() = this > 0
val Int.isNonPositive get() = this <= 0
val Int.isNegative get() = this < 0
val Int.isNonNegative get() = this >= 0


public val Number.dp2px: Float
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


private val suffixes: NavigableMap<Long, String> = TreeMap<Long, String>().apply{
    this[1_000L] = "k"
    this[1_000_000L] = "M"
    this[1_000_000_000L] = "G"
    this[1_000_000_000_000L] = "T"
    this[1_000_000_000_000_000L] = "P"
    this[1_000_000_000_000_000_000L] = "E"
}



/**
 * Format long to K M G.
 * https://stackoverflow.com/questions/4753251/how-to-go-about-formatting-1200-to-1-2k-in-java
 */
fun Long.format2KMG(): String {
    //Long.MIN_VALUE == -Long.MIN_VALUE so we need an adjustment here
    if (this == Long.MIN_VALUE) return (Long.MIN_VALUE + 1).toLong().format2KMG()
    if (this < 0) return "-" + (-this).format2KMG()
    if (this < 1000) return this.toString() //deal with easy case
    val (divideBy, suffix) = @Suppress("RECEIVER_NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS") suffixes.floorEntry(this)
    val truncated = this / (divideBy / 10) //the number part of the output times 10
    val hasDecimal = truncated < 100 && truncated / 10.0 != (truncated / 10).toDouble()
    return if (hasDecimal) (truncated / 10.0).toString() + suffix else (truncated / 10).toString() + suffix
}
