package me.yangxiaobin.android.kotlin.codelab.ext.animator

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.view.View
import androidx.core.view.doOnDetach


/**
 * @see androidx.core.animation
 */

enum class Anim(val propertyName: String) {
    ALPHA("alpha"),
    ROTATION("rotation"),
    ROTATION_X("rotationX"),
    ROTATION_Y("rotationY"),
    SCALE_X("scaleX"),
    SCALE_Y("scaleY"),
    TRANSLATION_X("translationX"),
    TRANSLATION_Y("translationY"),
    TRANSLATION_Z("translationZ"),
    X("x"),
    Y("y"),
    Z("z"),
}

@SuppressLint("Recycle")
operator fun Animator?.plus(animator: Animator?): Animator =
    when {
        this is AnimatorSet && animator is AnimatorSet -> {
            animator.childAnimations.forEach { this.playTogether(it) }
            this
        }
        this is AnimatorSet -> {
            this.playTogether(animator)
            this
        }
        animator is AnimatorSet -> {
            animator.playTogether(this)
            animator
        }
        else -> {
            val set = AnimatorSet()
            set.playTogether(this)
            set.playTogether(animator)
            set

        }
    }

public fun View.getObjAnim(
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

public fun View.getObjAnim(
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



public fun View.getObjAnim(
    animEnum: Anim,
    vararg values: Int,
    init: (ObjectAnimator.() -> Unit) = {},
): ObjectAnimator? = this.getObjAnim(animEnum.propertyName, intValues = values, init)

public fun View.getObjAnim(
    animEnum: Anim,
    vararg values: Float,
    init: (ObjectAnimator.() -> Unit) = {},
): ObjectAnimator? = this.getObjAnim(animEnum.propertyName, floatValues = values, init)