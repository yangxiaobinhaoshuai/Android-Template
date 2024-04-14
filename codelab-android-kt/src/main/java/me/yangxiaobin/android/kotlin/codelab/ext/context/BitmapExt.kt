package me.yangxiaobin.android.kotlin.codelab.ext.context

import android.graphics.Bitmap
import android.graphics.Matrix


/**
 * x 轴 或 y 轴 翻转
 */
fun Bitmap.getFlippedBitmap(xFlip: Boolean = false, yFlip: Boolean = false): Bitmap {
    val source = this
    val matrix = Matrix()

    matrix.postScale(
        (if (xFlip) -1 else 1).toFloat(),
        (if (yFlip) -1 else 1).toFloat(),
        source.width / 2f,
        source.height / 2f
    )
    return Bitmap.createBitmap(source, 0, 0, source.width, source.height, matrix, true)
}
