package com.yxb.simple.adapters

import android.view.GestureDetector
import android.view.GestureDetector.SimpleOnGestureListener
import android.view.MotionEvent
import android.view.ScaleGestureDetector

/**
 * @see ScaleGestureDetector.OnScaleGestureListener
 */
typealias ScaleGestureListenerAdapter = ScaleGestureDetector.SimpleOnScaleGestureListener


/**
 * @see android.view.GestureDetector.OnGestureListener
 */
typealias OnGestureListenerAdapter = SimpleOnGestureListener

fun interface OnSingleTapListener : GestureDetector.OnGestureListener {
    fun onSingleTap(e: MotionEvent): Boolean

    override fun onDown(e: MotionEvent): Boolean = false

    override fun onShowPress(e: MotionEvent) = Unit

    override fun onSingleTapUp(e: MotionEvent): Boolean = onSingleTap(e)

    override fun onScroll(
        e1: MotionEvent,
        e2: MotionEvent,
        distanceX: Float,
        distanceY: Float,
    ): Boolean = false

    override fun onLongPress(e: MotionEvent) = Unit
    override fun onFling(
        e1: MotionEvent,
        e2: MotionEvent,
        velocityX: Float,
        velocityY: Float,
    ): Boolean = false
}
