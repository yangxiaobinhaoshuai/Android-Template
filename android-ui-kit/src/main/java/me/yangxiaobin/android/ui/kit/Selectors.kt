package me.yangxiaobin.android.ui.kit

import android.content.Context
import android.content.res.Resources
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.StateListDrawable
import android.view.View
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import me.yangxiaobin.android.kotlin.codelab.androidColor
import me.yangxiaobin.android.kotlin.codelab.ext.dp2px


fun createShapeDrawable(
    context: Context,
    @ColorRes solidColorRes: Int,
    topLeftRadiusDp: Int = 0,
    topRightRadiusDp: Int = 0,
    bottomLeftRadiusDp: Int = 0,
    bottomRightRadiusDp: Int = 0,
    radiusDp: Int = 0,
    strokeWidth: Int = 0,
    @ColorRes borderColorRes: Int = 0
): Drawable = createShapeDrawable(
    solidColor = solidColorRes.asColorInt(context),
    topLeftRadiusDp = topLeftRadiusDp,
    topRightRadiusDp = topRightRadiusDp,
    bottomLeftRadiusDp = bottomLeftRadiusDp,
    bottomRightRadiusDp = bottomRightRadiusDp,
    radiusDp = radiusDp,
    strokeWidth = strokeWidth,
    borderColor = if (borderColorRes != 0) borderColorRes.asColorInt(context) else 0
)

/**
 * Create shape : https://stackoverflow.com/questions/28578701/how-to-create-android-shape-background-programmatically
 */
fun createShapeDrawable(
    @ColorInt solidColor: Int = android.graphics.Color.WHITE,
    topLeftRadiusDp: Int = 0,
    topRightRadiusDp: Int = 0,
    bottomLeftRadiusDp: Int = 0,
    bottomRightRadiusDp: Int = 0,
    radiusDp: Int = 0,
    strokeWidth: Int = 0,
    @ColorInt borderColor: Int = 0
): Drawable {

    val shape = GradientDrawable()
    shape.shape = GradientDrawable.RECTANGLE

    val tlRadius = if (radiusDp > 0 ) radiusDp.dp2px else topLeftRadiusDp.dp2px
    val trRadius = if (radiusDp > 0 ) radiusDp.dp2px else topRightRadiusDp.dp2px
    val blRadius = if (radiusDp > 0 ) radiusDp.dp2px else bottomLeftRadiusDp.dp2px
    val brRadius = if (radiusDp > 0 ) radiusDp.dp2px else bottomRightRadiusDp.dp2px

    // docs of cornerRadii : https://developer.android.com/reference/android/graphics/drawable/GradientDrawable.html#setCornerRadii(float[])
    // top-left, top-right, bottom-right, bottom-left.
    shape.cornerRadii = floatArrayOf(
        tlRadius, tlRadius,
        trRadius, trRadius,
        blRadius, blRadius,
        brRadius, brRadius
    )

    if (solidColor != 0) shape.setColor(solidColor)

    if (strokeWidth > 0) shape.setStroke(strokeWidth, borderColor)

    return shape
}


/**
 * Create selector programmatically https://stackoverflow.com/questions/32226232/android-create-selector-programmatically
 */
fun View?.setSelectorBackground(
    onPressDrawable: Drawable,
    onNormalDrawable: Drawable,
    ignoreOriginalBackground: Boolean = false,
    drawableProvider: (Drawable) -> Unit = {},
) {
    if (this == null) return

    if (!ignoreOriginalBackground && this.background != null) throw IllegalStateException("Original background is NOT null.")

    val drawable = StateListDrawable()
    //drawable.setExitFadeDuration(400)
    drawable.addState(intArrayOf(android.R.attr.state_pressed), onPressDrawable)
    drawable.addState(intArrayOf(), onNormalDrawable)

    drawableProvider.invoke(drawable)

    this.background = drawable
}

fun View?.setSelectorColor(
    @ColorInt onPressColor: Int,
    @ColorInt onNormalColor: Int = androidColor.TRANSPARENT,
    ignoreOriginalBackground: Boolean = false,
    drawableProvider: (Drawable) -> Unit = {},
) = setSelectorBackground(
    onPressDrawable = ColorDrawable(onPressColor),
    onNormalDrawable = ColorDrawable(onNormalColor),
    ignoreOriginalBackground = ignoreOriginalBackground,
    drawableProvider = drawableProvider,
)


fun View?.setSelectorColorRes(
    @ColorRes onPressColorRes: Int,
    @ColorRes onNormalColorRes: Int,
    ignoreOriginalBackground: Boolean = false,
    drawableProvider: (Drawable) -> Unit = {},
) = setSelectorColor(
    onPressColor = onPressColorRes.asColorInt(requireNotNull(this).context),
    onNormalColor = onNormalColorRes.asColorInt(requireNotNull(this).context),
    ignoreOriginalBackground = ignoreOriginalBackground,
    drawableProvider = drawableProvider,
)

@ColorInt
private fun @receiver:ColorRes Int.asColorInt(context: Context): Int = try {
    ContextCompat.getColor(context, this)
} catch (e: Exception) {
    e.printStackTrace()
    val resName = runCatching { context.resources.getResourceEntryName(this) }.getOrNull()
    throw Resources.NotFoundException("Res Id for ${resName ?: this} not found.")
}
