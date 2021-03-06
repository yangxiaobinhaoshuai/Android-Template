package me.yangxiaobin.android.kotlin.codelab.ext

import android.content.Context
import android.graphics.Rect
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.core.view.doOnDetach
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flow


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
val View.getGlobalVisibleRect: Rect
    get() {
        val r = Rect()
        this.getGlobalVisibleRect(r)
        return r
    }

/**
 * 参照系是 view 自身的左上角
 */
val View.getLocalVisibleRect: Rect
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
 * Copy from
 * @see com.google.android.material.internal.ViewUtils.requestFocusAndShowKeyboard
 */
fun View.requestFocusAndShowKeyboard() {
    this.requestFocus()
    this.post {
        val inputMethodManager = this.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.showSoftInput(this, InputMethodManager.SHOW_IMPLICIT)
    }
}
