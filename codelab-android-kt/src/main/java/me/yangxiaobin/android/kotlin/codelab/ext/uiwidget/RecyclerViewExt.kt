package me.yangxiaobin.android.kotlin.codelab.ext.uiwidget

import android.content.Context
import android.view.MotionEvent
import android.view.View
import android.view.ViewConfiguration
import androidx.core.view.children
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import me.yangxiaobin.android.kotlin.codelab.ext.Action
import me.yangxiaobin.android.kotlin.codelab.ext.emptyAction
import me.yangxiaobin.android.kotlin.codelab.ext.isOnView
import me.yangxiaobin.android.kotlin.codelab.log.L
import me.yangxiaobin.logger.core.LogLevel
import me.yangxiaobin.logger.log
import kotlin.math.abs


private val logI = L.log(LogLevel.INFO, "rv-ext")

val Int.toRecyclerViewScrollStateString
    get() = when (this) {
        RecyclerView.SCROLL_STATE_IDLE -> "IDLE"
        RecyclerView.SCROLL_STATE_DRAGGING -> "DRAGGING"
        RecyclerView.SCROLL_STATE_SETTLING -> "SETTLING"
        else -> throw IllegalArgumentException("Unexpected scroll state int $this.")
    }

typealias OnRvLongItemClickListener = (Pair<View, Int>) -> Boolean
typealias OnRvItemClickListener = (Pair<View, Int>) -> Unit

public fun RecyclerView.setSimpleDivider(orientation: Int = RecyclerView.VERTICAL) = apply {
    this.addItemDecoration(DividerItemDecoration(this.context, orientation))
}

class RvClickListener(
    context: Context,
    private val onLongClick: OnRvLongItemClickListener? = null,
    private val onClick: OnRvItemClickListener? = null,
) : RecyclerView.SimpleOnItemTouchListener() {

    private val configuration by lazy { ViewConfiguration.get(context) }
    // 21 in Samsung Galaxy S21 设备上
    // 24 in Oppo reno android 11
    private val touchSlop: Float by lazy { configuration.scaledTouchSlop.toFloat() }

    private var posRecord: Pair<Float, Float> = -1F to -1F
    private var canClickPerformed = true
    private var downTimestamp: Long = 0L

    private var hasPerformedLongClick: Boolean? = null

    // 400 or 500 ms
    // 400 in oppo
    // 500 in samsung
    private val longClickTimeout by lazy { ViewConfiguration.getLongPressTimeout().toLong() }


    private var longClickAction: Runnable? = null

    init {
        logI("touchSlop: $touchSlop, longClickTimeout: $longClickTimeout")
    }


    override fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent): Boolean {

        val lm: RecyclerView.LayoutManager = rv.layoutManager ?: return false


        when (e.action) {

            MotionEvent.ACTION_DOWN -> {
                canClickPerformed = true
                hasPerformedLongClick = null
                posRecord = posRecord.copy(e.rawX, e.rawY)
                downTimestamp = System.currentTimeMillis()

                rv.children.find { e.isOnView(it) }
                    .also { if (it == null) logI("Can't find target view in action down") }
                    ?.let { targetView ->

                        val pos: Int = lm.getPosition(targetView)

                        if (enableItemLongClick) {
                            longClickAction = Runnable{ hasPerformedLongClick = onLongClick?.invoke(targetView to pos) }
                            rv.postDelayed(longClickAction, longClickTimeout)
                        }
                    }

            }

            MotionEvent.ACTION_UP -> {

                if (canClickPerformed) {

                    val (x: Float, y: Float) = posRecord

                    // Samsung Galaxy S21 设备上
                    // x = 707
                    // e.rawX = 705
                    // 会有细微差距
                    if (abs(x - e.rawX) < touchSlop && abs(y - e.rawY) < touchSlop) {

                        rv.children.find { e.isOnView(it) }
                            .also { if (it == null) logI("Can't find target view in action up.") }
                            ?.let { targetView ->

                                val pos = lm.getPosition(targetView)

                                if (hasPerformedLongClick != false && enableItemClick) {
                                    onClick?.invoke(targetView to pos)
                                    rv.removeCallbacks(longClickAction)
                                }
                            }
                    }
                }
            }
            else -> {

                val (x: Float, y: Float) = posRecord

                if (x >= 0 && y >= 0) {

                    if (abs(e.rawY - y) > touchSlop || abs(e.rawX - x) > touchSlop) {
                        canClickPerformed = false
                        rv.removeCallbacks(longClickAction)
                    }

                }

            }
        }
        return false
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
) = apply { this.addOnItemTouchListener(RvClickListener(this.context, onLongClick, onClick)) }


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
    this.addOnItemTouchListener(RvClickListener(this.context, onLongClick, onClick))
}
