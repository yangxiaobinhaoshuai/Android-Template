package me.yangxiaobin.android.kotlin.codelab.ext

import android.content.Context
import android.content.ContextWrapper
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat


val Context.inflater: LayoutInflater get() = android.view.LayoutInflater.from(this)


inline fun <reified T : Context> Context.findBaseContext(): T? {
    var ctx: Context? = this
    do {
        if (ctx is T) {
            return ctx
        }
        if (ctx is ContextWrapper) {
            ctx = ctx.baseContext
        }
    } while (ctx != null)

    // If we reach here, there's not an Context of type T in our Context hierarchy
    return null
}

fun @receiver:StringRes Int.toResString(context: Context): String = context.getString(this)

/**
 * val intoString: Int.() -> String = buildResString(context)
 * val text = R.string.abc.intoString()
 */
fun buildResString(context: Context): Int.() -> String = fun @receiver:StringRes Int.(): String = context.getString(this)

@ColorInt
fun @receiver:ColorRes Int.toResColor(context: Context): Int = ContextCompat.getColor(context, this)

fun @receiver:DrawableRes Int.toResDrawable(context: Context): Drawable = requireNotNull(ContextCompat.getDrawable(context, this)){"toResDrawable null."}


fun Context.hasPermission(permission: String) = ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED
