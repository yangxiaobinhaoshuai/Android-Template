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