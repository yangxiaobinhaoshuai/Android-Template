package me.yangxiaobin.android.kotlin.codelab.ext

import android.annotation.SuppressLint
import android.graphics.Rect
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View

fun MotionEvent.isOnView(v: View): Boolean {

    val motionRawX = this.rawX
    val motionRawY = this.rawY

    val rect = Rect()
    // 要考虑 View 发生旋转，缩放的情况
    v.getHitRect(rect)

    return rect.contains(motionRawX.toInt(),motionRawY.toInt())
}

val MotionEvent?.getActionString: String get() = if (this != null) MotionEvent.actionToString(this.action) else ""



typealias OnTouchCondition <T> = (MotionEvent) -> T
typealias OnTouchAction = OnTouchCondition<Unit>
typealias OnTouchInterception = OnTouchCondition<Boolean>

/**
 * 当 onClick 不方便设置时候可以用 onTapUp
 */
@SuppressLint("ClickableViewAccessibility")
fun View.onTapUp(onTapUp: OnTouchAction) {

    val tabListener: GestureDetector.SimpleOnGestureListener by lazy {
        object : GestureDetector.SimpleOnGestureListener() {

            override fun onSingleTapUp(e: MotionEvent): Boolean {
                onTapUp.invoke(e)
                return super.onSingleTapUp(e)
            }
        }
    }

    val gestureDetector: GestureDetector by lazy { GestureDetector(this.context, tabListener) }

    this.setOnTouchListener { _, event: MotionEvent ->
        gestureDetector.onTouchEvent(event)
        return@setOnTouchListener false
    }
}

@SuppressLint("ClickableViewAccessibility")
fun View.doOnTouch(onTouch: OnTouchAction) {
    this.setOnTouchListener { _, event ->
        onTouch.invoke(event)
        return@setOnTouchListener false
    }
}

@SuppressLint("ClickableViewAccessibility")
public fun View.doOnInterceptTouch(intercept: OnTouchInterception) {
    this.setOnTouchListener { _, event ->
        return@setOnTouchListener intercept.invoke(event)
    }
}