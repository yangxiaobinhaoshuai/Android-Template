package me.yangxiaobin.android.kotlin.codelab.ext.animator

import android.animation.ObjectAnimator
import android.view.View
import androidx.core.view.doOnDetach


/**
 * @see androidx.core.animation
 */

enum class Anim(val propertyName: String) {
    Alpha("alpha"),
    Rotation("rotation"),
    RotationX("rotationX"),
    RotationY("rotationY"),
    ScaleX("scaleX"),
    ScaleY("scaleY"),
    TranslationX("translationX"),
    TranslationY("translationY"),
    TranslationZ("translationZ"),
    X("x"),
    Y("y"),
    Z("z"),
}

fun View.createObjAnimator(
    propName: String,
    vararg intValues: Int,
    objAnimConfig: (ObjectAnimator.() -> Unit) = {},
): ObjectAnimator? {
    val animator: ObjectAnimator? = try {
        ObjectAnimator.ofInt(this, propName, *intValues).also(objAnimConfig)
    } catch (e: Exception) {
        null
    }
    this.doOnDetach { animator?.cancel() }
    return animator
}

fun View.createObjAnimator(
    propName: String,
    vararg floatValues: Float,
    objAnimConfig: (ObjectAnimator.() -> Unit) = {},
): ObjectAnimator? {
    val animator: ObjectAnimator? = try {
        ObjectAnimator.ofFloat(this, propName, *floatValues).also(objAnimConfig)
    } catch (e: Exception) {
        null
    }
    this.doOnDetach { animator?.cancel() }
    return animator
}

fun View.createObjAnimator(
    animEnum: Anim,
    vararg values: Int,
    init: (ObjectAnimator.() -> Unit) = {},
): ObjectAnimator? = this.createObjAnimator(animEnum.propertyName, intValues = values, init)

fun View.createObjAnimator(
    animEnum: Anim,
    vararg values: Float,
    init: (ObjectAnimator.() -> Unit) = {},
): ObjectAnimator? = this.createObjAnimator(animEnum.propertyName, floatValues = values, init)