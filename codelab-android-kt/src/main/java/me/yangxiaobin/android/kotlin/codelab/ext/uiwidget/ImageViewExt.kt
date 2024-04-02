package me.yangxiaobin.android.kotlin.codelab.ext.uiwidget

import android.content.res.ColorStateList
import android.widget.ImageView
import androidx.core.content.ContextCompat


fun ImageView.setTintColor(colorInt: Int) {
    val color = ContextCompat.getColor(this.context, colorInt)
    this.imageTintList = ColorStateList.valueOf(color)
}
