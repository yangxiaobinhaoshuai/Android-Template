package me.yangxiaobin.android.kotlin.codelab.ext.context

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
import java.util.concurrent.Executor
import java.util.concurrent.Executors


val Context.screenSize: Pair<Int, Int> get() = this.resources.screenSize
val Context.statusBarSize: Int get() = this.resources.statusBarSize
val Context.navigationBarSize: Int get() = this.resources.navigationBarSize

val Context.inflater: LayoutInflater get() = android.view.LayoutInflater.from(this)



fun Context.createMainExecutor(): Executor = ContextCompat.getMainExecutor(this)

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


/**
 * val intoString: Int.() -> String = buildResString(context)
 * val text = R.string.abc.intoString()
 */
fun buildResString(context: Context): Int.() -> String
        = fun @receiver:StringRes Int.(): String
        = context.getString(this)

fun buildResColor(context: Context): Int.() -> Int
        = fun @receiver:ColorRes Int.(): Int
        = ContextCompat.getColor(context, this)

fun buildResDrawable(context: Context): Int.() -> Drawable
        = fun @receiver:ColorRes Int.(): Drawable
        = requireNotNull(ContextCompat.getDrawable(context, this)) { "Can NOT found drawable for ${this.getResName(context)}." }


fun @receiver:StringRes Int.toResString(context: Context): String = context.getString(this)

@ColorInt
fun @receiver:ColorRes Int.toResColor(context: Context): Int = ContextCompat.getColor(context, this)

fun @receiver:DrawableRes Int.toResDrawable(context: Context): Drawable = requireNotNull(ContextCompat.getDrawable(context, this)) { "Can NOT found drawable for ${this.getResName(context)}." }




fun Context.hasPermission(permission: String): Boolean = ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED

fun Int.getResName(context: Context): String? = context.resources.getResName(this)
