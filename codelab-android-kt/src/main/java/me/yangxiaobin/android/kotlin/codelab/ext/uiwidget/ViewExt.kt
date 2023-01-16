package me.yangxiaobin.android.kotlin.codelab.ext.uiwidget

import android.graphics.Rect
import android.view.MotionEvent
import android.view.View
import androidx.core.view.doOnDetach
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flow
import me.yangxiaobin.android.kotlin.codelab.ext.doOnInterceptTouch


/**
 * 参照系是屏幕
 */
val View.getScreenLocation: Pair<Int, Int>
    get() {
        val posPair = IntArray(2)
        this.getLocationOnScreen(posPair)
        return posPair[0] to posPair[1]
    }

/**
 * 参照系是 Window
 */
val View.getWindowLocation: Pair<Int, Int>
    get() {
        val posPair = IntArray(2)
        this.getLocationInWindow(posPair)
        return posPair[0] to posPair[1]
    }

/**
 * 参照系是屏幕
 */
val View.globalVisibleRect: Rect
    get() {
        val r = Rect()
        this.getGlobalVisibleRect(r)
        return r
    }

/**
 * 参照系是 view 自身的左上角
 */
val View.localVisibleRect: Rect
    get() {
        val r = Rect()
        this.getLocalVisibleRect(r)
        return r
    }


/**
 * 获取 View 的 res Name, 如 : R.string.user_name -> user_name
 */
val View.getResName: String?
    get() {
        if (this.id == View.NO_ID) return null
        return runCatching { this.context.resources.getResourceEntryName(this.id) }.getOrNull()
    }


fun View.clicks(): Flow<Unit> = callbackFlow {
    setOnClickListener {
        trySend(Unit)
    }
    awaitClose { setOnClickListener(null) }
}

/**
 * Also see [Flow.debounce]
 */
fun <T> Flow<T>.throttleFirst(windowDuration: Long): Flow<T> = flow {
    var lastEmissionTime = 0L
    collect { upstream ->
        val currentTime = System.currentTimeMillis()
        val mayEmit = currentTime - lastEmissionTime > windowDuration
        if (mayEmit) {
            lastEmissionTime = currentTime
            emit(upstream)
        }
    }
}


private object Debouncer {
    @Volatile
    private var enabled: Boolean = true
    private val enableAgain = Runnable { enabled = true }

    fun canPerform(view: View, delay: Long): Boolean {
        if (enabled) {
            enabled = false
            view.postDelayed(enableAgain, delay)

            view.doOnDetach {
                view.removeCallbacks(enableAgain)
            }

            return true
        }
        return false
    }
}

fun <T : View> T.onClickDebounced(delay: Long = 100, click: (view: T) -> Unit) {
    setOnClickListener {
        if (Debouncer.canPerform(it, delay)) {
            @Suppress("UNCHECKED_CAST")
            click(it as T)
        }

    }
}

/**
 * NB. 如果 View 还没有 attach，就会直接执行
 */
fun View.postCancellable(r: Runnable) {
    this.post(r)
    this.doOnDetach { it.removeCallbacks(r) }
}

/**
 * 使子 View 能够跟随手指拖拽
 *
 * @see https://stackoverflow.com/a/31094315/10247834
 */
fun View.makeDraggable(){

    var dx: Float = 0F
    var dy: Float = 0F
    this.doOnInterceptTouch { event: MotionEvent ->

        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                dx = this.x - event.rawX
                dy = this.y - event.rawY
            }
            MotionEvent.ACTION_MOVE -> {
                this.animate()
                    .x(event.rawX + dx)
                    .y(event.rawY + dy)
                    .setDuration(0)
                    .start()
            }
            else -> {
                dx = 0F
                dy = 0F
            }
        }

        return@doOnInterceptTouch true
    }
}