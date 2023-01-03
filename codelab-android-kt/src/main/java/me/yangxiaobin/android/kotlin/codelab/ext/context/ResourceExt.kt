package me.yangxiaobin.android.kotlin.codelab.ext.context

import android.content.res.Resources


/**
 * Resources.getSystem() vs context.getResource
 *
 * It comes with a downside though.
 * Resources.getSystem() provides access to only system resources (no application resources),
 * and is not configured for the current screen (can * not use dimension units, does not change based on orientation, etc).
 */


/**
 * Return a global shared Resources object that provides access to only system resources (no application resources),
 * is not configured for the current screen (can not use dimension units,does not change based on orientation, etc),
 * and is not affected by Runtime Resource Overlay.
 */
val systemResource: Resources get() = Resources.getSystem()

val screenSize: Pair<Int, Int> get() = systemResource.screenSize
val statusBarSize: Int get() = systemResource.statusBarSize
val navigationBarSize: Int get() = systemResource.navigationBarSize

val Resources.screenSize: Pair<Int, Int> get() = this.displayMetrics.let { it.widthPixels to it.heightPixels }

@Deprecated("Using internal inset dimension resource status_bar_height is not supported")
val Resources.statusBarSize: Int
    get() {
        var result = 0
        val resourceId = this.getIdentifier("status_bar_height", "dimen", "android")
        if (resourceId > 0) {
            result = this.getDimensionPixelSize(resourceId)
        }
        return result
    }

@Deprecated("Using internal inset dimension resource status_bar_height is not supported")
val Resources.navigationBarSize: Int
    get() {
        val resourceId = this.getIdentifier("navigation_bar_height", "dimen", "android")
        var height = this.getDimensionPixelSize(resourceId)
        if (height < 0) {
            height = 0
        }
        return height
    }


fun Resources.getResName(resId: Int): String? = runCatching { this.getResourceEntryName(resId) }.getOrNull()