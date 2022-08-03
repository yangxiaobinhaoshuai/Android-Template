package me.yangxiaobin.android.kotlin.codelab.ext

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
import me.yangxiaobin.android.kotlin.codelab.log.L
import me.yangxiaobin.logger.core.LogLevel
import me.yangxiaobin.logger.log
import kotlin.math.abs


private val logI = L.log(LogLevel.INFO, "codeLab-ext")

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

class RvClickListener(
    context: Context,
    private val onLongClick: OnRvLongItemClickListener? = null,
    private val onClick: OnRvItemClickListener? = null,
) : RecyclerView.SimpleOnItemTouchListener() {

    private val configuration by lazy { ViewConfiguration.get(context) }

    private var posRecord: Pair<Float, Float> = -1F to -1F
    private var canClickPerformed = true
    private var downTimestamp: Long = 0L

    private var hasPerformedLongClick: Boolean? = null

    private val longClickTimeout by lazy { ViewConfiguration.getLongPressTimeout().toLong() }

    private var longClickAction: Runnable? = null


    override fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent): Boolean {

        val lm = rv.layoutManager ?: return false


        when (e.action) {

            MotionEvent.ACTION_DOWN -> {
                canClickPerformed = true
                hasPerformedLongClick = null
                posRecord = posRecord.copy(e.rawX, e.rawY)
                downTimestamp = System.currentTimeMillis()

                rv.children.find { e.isOnView(it) }
                    ?.let { targetView ->

                        val pos = lm.getPosition(targetView)

                        if (enableItemLongClick) {
                            longClickAction = Runnable{ hasPerformedLongClick = onLongClick?.invoke(targetView to pos) }
                            rv.postDelayed(longClickAction, longClickTimeout)
                        }
                    }

            }

            MotionEvent.ACTION_UP -> {

                if (canClickPerformed) {

                    val (x, y) = posRecord
                    if (x == e.rawX && y == e.rawY) {

                        rv.children.find { e.isOnView(it) }
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

                val (x, y) = posRecord

                if (x >= 0 && y >= 0) {

                    if (abs(e.rawY - y) > configuration.scaledTouchSlop || abs(e.rawX - x) > configuration.scaledTouchSlop) {
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
