package me.yangxiaobin.android.kotlin.codelab.ext.context

import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.graphics.drawable.LayerDrawable
import androidx.core.graphics.drawable.DrawableCompat


/**
 * Also see [me.yangxiaobin.android.ui.kit.Selectors.kt]
 * Also see [android.graphics.drawable.DrawableWrapper]
 */

fun Drawable.transformTo(t: (Drawable) -> Drawable): Drawable = t.invoke(this)

fun Drawable.wrapped(wrapper: Drawable.() -> Unit): Drawable {
    val unwrappedDrawable: Drawable = this
    val wrappedDrawable: Drawable = DrawableCompat.wrap(unwrappedDrawable)
    wrapper.invoke(wrappedDrawable)
    return wrappedDrawable
}

fun Drawable.tint(colorInt: Int): Drawable = this.wrapped { this.setTint(colorInt) }

/**
 * @param angle Clockwise degree
 */
public fun Drawable.rotated(angle: Float): Drawable = this.transformTo {
    val arD = arrayOf(this)
    object : LayerDrawable(arD) {
        override fun draw(canvas: Canvas) {
            canvas.save()
            canvas.rotate(angle, this.bounds.width().toFloat() / 2, this.bounds.height().toFloat() / 2)
            super.draw(canvas)
            canvas.restore()
        }
    }
}
