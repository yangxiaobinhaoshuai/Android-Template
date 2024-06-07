package me.yangxiaobin.android.kotlin.codelab.ext

import android.annotation.SuppressLint
import android.graphics.Matrix
import android.graphics.Rect
import android.os.SystemClock
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View

/**
 * From chatgpt 3.5
 */
fun MotionEvent.isOnView(v: View): Boolean {

    val viewLocation = IntArray(2)
    v.getLocationOnScreen(viewLocation)

    val viewX = viewLocation[0]
    val viewY = viewLocation[1]

    val invertedMatrix = Matrix()
    v.matrix.invert(invertedMatrix)

    val eventXY = floatArrayOf(this.rawX, this.rawY)
    invertedMatrix.mapPoints(eventXY)

    val eventX = eventXY[0].toInt()
    val eventY = eventXY[1].toInt()

    return eventX >= viewX && eventX < viewX + v.width &&
            eventY >= viewY && eventY < viewY + v.height
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

/**
 * x :触摸事件的 X 坐标
 * y :触摸事件的 Y 坐标
 */
fun View.mockTouchEvents(
    downX: Float, downY: Float,
    xOffset: Float = 100F,
    yOffset: Float = 100F,
    upX: Float = downX, upY: Float = downY,
) {

    // 模拟触摸按下事件
    val downTime = SystemClock.uptimeMillis()
    val eventTime = SystemClock.uptimeMillis()

    val action = MotionEvent.ACTION_DOWN
    val motionEvent = MotionEvent.obtain(
        downTime, eventTime, action, downX, downY, 0
    )
    this.dispatchTouchEvent(motionEvent)

    // 模拟触摸移动事件
    val moveX = downX + xOffset // 移动到的 X 坐标
    val moveY = downY + yOffset // 移动到的 Y 坐标
    val moveAction = MotionEvent.ACTION_MOVE
    val moveEvent = MotionEvent.obtain(
        downTime, eventTime, moveAction, moveX, moveY, 0
    )
    this.dispatchTouchEvent(moveEvent)

    // 模拟触摸抬起事件
    val upAction = MotionEvent.ACTION_UP
    val upEvent = MotionEvent.obtain(
        downTime, eventTime, upAction, upX, upY, 0
    )
    this.dispatchTouchEvent(upEvent)
}
