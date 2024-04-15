package me.yangxiaobin.android.kotlin.codelab.ext.context

import android.graphics.Paint
import android.graphics.Rect


fun Paint.getTextBounds(text: String): Rect {
    val textBounds = Rect()
    this.getTextBounds(text, 0, text.length, textBounds)
    return textBounds
}
