package me.yangxiaobin.android.kotlin.codelab.ext.uiwidget

import android.content.Context
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import me.yangxiaobin.android.kotlin.codelab.ext.Action
import me.yangxiaobin.android.kotlin.codelab.ext.emptyAction
import me.yangxiaobin.android.kotlin.codelab.log.L
import me.yangxiaobin.logger.core.LogLevel
import me.yangxiaobin.logger.log


private val logI = L.log(LogLevel.INFO, "rv-ext")


fun convertRecyclerViewScrollStateToString(state: Int): String {
    return when (state) {
        RecyclerView.SCROLL_STATE_IDLE -> "IDLE"
        RecyclerView.SCROLL_STATE_DRAGGING -> "DRAGGING"
        RecyclerView.SCROLL_STATE_SETTLING -> "SETTLING"
        else -> throw IllegalArgumentException("Unexpected scroll state int $state.")
    }
}


typealias OnRvLongItemClickListener = (Pair<View, Int>) -> Boolean
typealias OnRvItemClickListener = (Pair<View, Int>) -> Unit

public fun RecyclerView.setSimpleDivider(orientation: Int = RecyclerView.VERTICAL) = apply {
    this.addItemDecoration(DividerItemDecoration(this.context, orientation))
}


// TODO to be verified
class RvClickListener(
    context: Context,
    private val recyclerView: RecyclerView,
    private val onClick: OnRvItemClickListener? = null,
    private val onLongClick: OnRvLongItemClickListener? = null
) : RecyclerView.SimpleOnItemTouchListener() {

    private val gestureDetector: GestureDetector

    init {
        gestureDetector = GestureDetector(context, object : GestureDetector.SimpleOnGestureListener() {

            // 单击确认时触发（在双击超时后），避免与双击冲突
            override fun onSingleTapConfirmed(e: MotionEvent): Boolean {
                findChildView(e)?.let { view ->
                    val position = recyclerView.getChildAdapterPosition(view)
                    if (position != RecyclerView.NO_POSITION) {
                        onClick?.invoke(view to position)
                        return true // 事件已消费
                    }
                }
                return false
            }

            // 长按时触发
            override fun onLongPress(e: MotionEvent) {
                findChildView(e)?.let { view ->
                    val position = recyclerView.getChildAdapterPosition(view)
                    if (position != RecyclerView.NO_POSITION) {
                        onLongClick?.invoke(view to position)
                    }
                }
            }
        })
    }

    override fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent): Boolean {
        // 将所有触摸事件都传递给 GestureDetector
        findChildView(e)?.let {
            // 如果 GestureDetector 认为这是一个需要处理的手势，就拦截事件
            if (gestureDetector.onTouchEvent(e)) {
                return true
            }
        }
        return false
    }

    private fun findChildView(e: MotionEvent): View? {
        return recyclerView.findChildViewUnder(e.x, e.y)
    }
}

private var enableItemLongClick = true
private var enableItemClick = true

fun RecyclerView.enableItemLongClick() = apply { enableItemLongClick = true }
fun RecyclerView.disableItemLongClick() = apply { enableItemLongClick = false }

fun RecyclerView.enableItemClick() = apply { enableItemClick = true }
fun RecyclerView.disableItemClick() = apply { enableItemClick = false }

fun RecyclerView.setOnItemClickListener(
    onLongClick: OnRvLongItemClickListener? = null,
    onClick: OnRvItemClickListener,
) = apply { this.addOnItemTouchListener(RvClickListener(this.context, this, onClick, onLongClick)) }


class RvClickObserver(
    private val enableAction: Action = emptyAction,
    private val disableAction: Action = emptyAction,
) : LifecycleObserver {
    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun enable() = enableAction.invoke()

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun disable() = disableAction.invoke()
}

fun RecyclerView.setOnItemClickListenerWithLifecycleOwner(
    longClickLifecycleOwner: LifecycleOwner,
    onLongClick: OnRvLongItemClickListener? = null,
    clickLifecycleOwner: LifecycleOwner,
    onClick: OnRvItemClickListener,
) = apply {

    val clickObserver = RvClickObserver(
        enableAction = { this.enableItemClick() },
        disableAction = { this.disableItemClick() }
    )

    val longClickObserver = RvClickObserver(
        enableAction = { this.enableItemLongClick() },
        disableAction = { this.disableItemLongClick() }
    )

    longClickLifecycleOwner.lifecycle.addObserver(longClickObserver)
    clickLifecycleOwner.lifecycle.addObserver(clickObserver)
    this.addOnItemTouchListener(RvClickListener(this.context, this, onClick,onLongClick))
}
