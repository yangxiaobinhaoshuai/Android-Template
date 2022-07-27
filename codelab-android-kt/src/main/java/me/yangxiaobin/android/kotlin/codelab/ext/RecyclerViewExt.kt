package me.yangxiaobin.android.kotlin.codelab.ext

import android.view.MotionEvent
import android.view.View
import android.view.ViewConfiguration
import androidx.core.view.children
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import me.yangxiaobin.android.kotlin.codelab.log.L
import me.yangxiaobin.logger.core.LogLevel
import me.yangxiaobin.logger.log
import kotlin.math.*


private val logI = L.log(LogLevel.INFO,"codeLab-ext")

val Int.toRecyclerViewScrollStateString
    get() = when (this) {
        RecyclerView.SCROLL_STATE_IDLE -> "IDLE"
        RecyclerView.SCROLL_STATE_DRAGGING -> "DRAGGING"
        RecyclerView.SCROLL_STATE_SETTLING -> "SETTLING"
        else -> throw IllegalArgumentException("Unexpected scroll state int $this.")
    }

typealias OnRvLongItemClickListener = (Pair<View, Int>) -> Boolean
typealias OnRvItemClickListener = (Pair<View, Int>) -> Unit

fun RecyclerView.setSimpleDivider(orientation: Int = RecyclerView.VERTICAL) = apply {
    this.addItemDecoration(DividerItemDecoration(this.context, orientation))
}

fun RecyclerView.setOnItemClickListener(
    onLongClick: OnRvLongItemClickListener? = null,
    onClick: OnRvItemClickListener,
) = apply {

    val configuration by lazy { ViewConfiguration.get(this.context) }

    this.addOnItemTouchListener(object : RecyclerView.OnItemTouchListener {

        private var posRecord: Pair<Float, Float> = -1F to -1F
        private var isClick = true
        private var downTimestamp: Long = 0L

        override fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent): Boolean {
            //logI("rv OnItemTouchListener onInterceptTouchEvent, e: $e")

            when (e.action) {

                MotionEvent.ACTION_DOWN -> {
                    isClick = true
                    posRecord = posRecord.copy(e.rawX, e.rawY)
                    downTimestamp = System.currentTimeMillis()
                }

                MotionEvent.ACTION_UP -> {
                    val timeIsLongEnough by lazy { System.currentTimeMillis() - downTimestamp >= ViewConfiguration.getLongPressTimeout() }

                    if (!isClick) return false

                    val (x, y) = posRecord
                    if (x == e.rawX && y == e.rawY) {

                        rv.children.find { e.isOnView(it) }
                            ?.let { targetView ->

                                val lm = rv.layoutManager ?: return false
                                val pos = lm.getPosition(targetView)

                                val skipClick =
                                    timeIsLongEnough && onLongClick?.invoke(targetView to pos) == true
                                if (!skipClick) onClick.invoke(targetView to pos)
                            }
                    }
                }
                else -> {
                    val (x, y) = posRecord

                    if (x >= 0 && y >= 0) {

                        if (abs(e.rawY - y) > configuration.scaledTouchSlop || abs(e.rawX - x) > configuration.scaledTouchSlop) isClick =
                            false

                    }

                }
            }
            return false
        }

        override fun onTouchEvent(rv: RecyclerView, e: MotionEvent) {}

        override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {}

    })

}
