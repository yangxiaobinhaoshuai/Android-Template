package me.yangxiaobin.android.kotlin.codelab.ext

import android.view.MotionEvent
import android.view.View
import me.yangxiaobin.android.kotlin.codelab.ext.uiwidget.getScreenLocation

fun MotionEvent.isOnView(v: View): Boolean {

    val motionRawX = this.rawX
    val motionRawY = this.rawY

    val (viewRawX,viewRawY) = v.getScreenLocation

    return (motionRawX >= viewRawX && motionRawX <= viewRawX + v.width) && (motionRawY >= viewRawY && motionRawY <= viewRawY + v.height)
}

val MotionEvent?.getActionString: String get() = if (this != null) MotionEvent.actionToString(this.action) else ""
