package me.yangxiaobin.android.kotlin.codelab.ext.animator

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.view.View
import android.view.animation.Animation


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
inline fun View.createObjAnimator(
    propName: String,
    vararg t: Int,
    objAnimConfig: (ObjectAnimator.() -> Unit) = {},
) {
    val animator = ObjectAnimator.ofInt(this, propName, *t)


}

fun View.createObjAnimator(
    anim: Anim,
    vararg values: Int,
    init: (ObjectAnimator.() -> Unit)? = null,
): ObjectAnimator =
    ObjectAnimator.ofInt(this, anim.propertyName, *values).also { init?.invoke(it) }

fun View.createObjAnimator(
    anim: Anim,
    vararg values: Float,
    init: (ObjectAnimator.() -> Unit)? = null,
): ObjectAnimator =
    ObjectAnimator.ofFloat(this, anim.propertyName, *values).also { init?.invoke(it) }

fun AnimatorSet.onAnimationEnd(onAnimationEnd: () -> Unit) {
    addListener(object : AnimatorListenerAdapter() {
        override fun onAnimationEnd(animation: Animator?) {
            super.onAnimationEnd(animation)
            onAnimationEnd()
        }
    })
}

fun Animation.onAnimationEnd(onAnimationEnd: () -> Unit) {
    setAnimationListener(object : Animation.AnimationListener {
        override fun onAnimationRepeat(p0: Animation?) {}
        override fun onAnimationEnd(p0: Animation?) {
            onAnimationEnd()
        }

        override fun onAnimationStart(p0: Animation?) {}
    })
}