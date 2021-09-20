package me.yangxiaobin.android.kotlin.codelab.ext

import android.view.MotionEvent
import android.view.View
import android.view.ViewConfiguration
import androidx.core.view.children
import androidx.recyclerview.widget.RecyclerView

// MotionEvent
fun MotionEvent.isOnView(v: View): Boolean {

    val motionRawX = this.rawX
    val motionRawY = this.rawY

    val posPair = IntArray(2)
    v.getLocationOnScreen(posPair)

    val viewRawX = posPair[0]
    val viewRawY = posPair[1]

    return (motionRawX >= viewRawX && motionRawX <= viewRawX + v.width)
            && (motionRawY >= viewRawY && motionRawY <= viewRawY + v.height)
}

val MotionEvent.getActionString: String
    get() = when (this.action) {
        MotionEvent.ACTION_DOWN -> "DOWN"
        MotionEvent.ACTION_MOVE -> "MOVE"
        MotionEvent.ACTION_UP -> "UP"
        MotionEvent.ACTION_CANCEL -> "CANCEL"
        else -> this.action.toString()
    }


// RecyclerView
// TODO
private val KEY_HAS_LONG_CLICK_LISTENER: Int = View.generateViewId() + 2 shl 24

typealias OnRvLongItemClickListener = (Pair<View, Int>) -> Boolean
typealias OnRvItemClickListener = (Pair<View, Int>) -> Unit

fun RecyclerView.setOnItemClickListener(
    onLongClick: OnRvLongItemClickListener? = null,
    onClick: OnRvItemClickListener,
) = apply {

    this.addOnItemTouchListener(object : RecyclerView.OnItemTouchListener {

        private var posRecord: Pair<Float, Float> = -1F to -1F
        private var isClick = true
        private var downTimestamp: Long = 0L

        override fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent): Boolean = true

        override fun onTouchEvent(rv: RecyclerView, e: MotionEvent) {
            when (e.action) {

                MotionEvent.ACTION_DOWN -> {
                    isClick = true
                    posRecord = posRecord.copy(e.rawX, e.rawY)
                    downTimestamp = System.currentTimeMillis()
                }

                MotionEvent.ACTION_UP -> {
                    val timeIsLongEnough by lazy { System.currentTimeMillis() - downTimestamp >= ViewConfiguration.getLongPressTimeout() }

                    if (!isClick) return

                    val (x, y) = posRecord
                    if (x == e.rawX && y == e.rawY) {

                        rv.children.find { e.isOnView(it) }
                            ?.let { targetView ->

                                val lm = rv.layoutManager ?: return
                                val pos = lm.getPosition(targetView)

                                val skipClick = timeIsLongEnough && onLongClick?.invoke(targetView to pos) == true
                                if (!skipClick) onClick.invoke(targetView to pos)
                            }
                    }
                }
                else -> { isClick = false }
            }
        }

        override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {}

    })

}
