package me.yangxiaobin.android.kotlin.codelab.ext.uiwidget

import android.R.color
import android.graphics.Color
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.IntRange
import me.yangxiaobin.android.kotlin.codelab.log.L


/**
 * Set opacity
 * @param 40 i.e.
 */
fun View.setOpacity(@IntRange(from = 0, to = 100) opacityPercent: Int): Unit {

    val alphaFloat  = (100 - opacityPercent) / 100F

    when (this) {
        is ImageView -> this.alpha = alphaFloat
        is TextView -> {
            val preTxtColor = this.currentTextColor

            val alpha = Color.alpha(preTxtColor)
            val blue = Color.blue(preTxtColor)
            val green = Color.green(preTxtColor)
            val red = Color.red(preTxtColor)

            val newAlpha = (alpha * alphaFloat).toInt()

            //L.d("View.setOpacity","fl: $alphaFloat, a: $alpha, newA: $newAlpha r: $red, g: $green, b: $blue")

            val newColor = Color.argb(newAlpha, red, green, blue)

            this.setTextColor(newColor)
        }

        else -> Unit
    }
}

