package me.yangxiaobin.android.kotlin.codelab.ext

import android.annotation.SuppressLint
import android.content.Context
import android.content.ContextWrapper
import android.content.pm.PackageManager
import android.content.res.Resources
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.view.LayoutInflater
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import java.util.concurrent.Executor


/**
 * Resources.getSystem() vs context.getResource
 *
 * It comes with a downside though.
 * Resources.getSystem() provides access to only system resources (no application resources),
 * and is not configured for the current screen (can * not use dimension units, does not change based on orientation, etc).
 */


val systemResource: Resources get() = Resources.getSystem()

val screenSize get() = systemResource.screenSize
val statusBarSize get() = systemResource.statusBarSize
val navigationBarSize get() = systemResource.navigationBarSize

val Context.screenSize get() = this.resources.screenSize
val Context.statusBarSize get() = this.resources.statusBarSize
val Context.navigationBarSize get() = this.resources.navigationBarSize

val Resources.screenSize: Pair<Int, Int> get() = this.displayMetrics.let { it.widthPixels to it.heightPixels }

val Resources.statusBarSize: Int
    get() {
        var result = 0
        val resourceId = this.getIdentifier("status_bar_height", "dimen", "android")
        if (resourceId > 0) {
            result = this.getDimensionPixelSize(resourceId)
        }
        return result
    }

val Resources.navigationBarSize: Int
    get() {
        val resourceId = this.getIdentifier("navigation_bar_height", "dimen", "android")
        var height = this.getDimensionPixelSize(resourceId)
        if (height < 0) {
            height = 0
        }
        return height
    }

val Context.inflater: LayoutInflater get() = android.view.LayoutInflater.from(this)

val Context.mainExecutor: Executor get() = ContextCompat.getMainExecutor(this)


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


/**
 * Permission <uses-permission android:name="android.permission.VIBRATE" /> required
 */
@SuppressLint("MissingPermission")
fun Context.vibrate(milliseconds: Long = 50) {
    val v = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        v.vibrate(VibrationEffect.createOneShot(milliseconds, VibrationEffect.DEFAULT_AMPLITUDE))
    } else {
        v.vibrate(milliseconds)
    }
}
