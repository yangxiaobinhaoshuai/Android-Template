package me.yangxiaobin.android.kotlin.codelab.ext.context

import android.graphics.drawable.Drawable
import androidx.core.graphics.drawable.DrawableCompat


fun Drawable.wrapped(wrapper: (Drawable) -> Unit): Drawable {
    val unwrappedDrawable: Drawable = this
    val wrappedDrawable: Drawable = DrawableCompat.wrap(unwrappedDrawable)
    wrapper.invoke(wrappedDrawable)
    return wrappedDrawable

}

fun Drawable.tint(colorInt: Int): Drawable = this.wrapped { it.setTint(colorInt) }