package me.yangxiaobin.android.kotlin.codelab.ext

import android.os.Handler
import android.os.HandlerThread
import android.os.Looper
import android.os.Message
import android.view.View
import androidx.lifecycle.LifecycleOwner
import me.yangxiaobin.android.kotlin.codelab.lifecycle.SimpleLifecycleObserver


val mainLopper: Looper by lazy { Looper.getMainLooper() }

val mainHandler: Handler by lazy { Handler(mainLopper) }

fun createMainHandler(handleMessage: (Message) -> Unit): Handler = object : Handler(mainLopper) {
    override fun handleMessage(msg: Message) {
        super.handleMessage(msg)
        handleMessage(msg)
    }
}

fun Handler.postDelayCancellable(lifecycleOwner: LifecycleOwner, delay: Long = 0L, action: Action) {

    this.postDelayed(action, delay)

    val simpleObserver = SimpleLifecycleObserver(
        onBackground = {
            this.removeCallbacks(action)
        }
    )

    lifecycleOwner.lifecycle.addObserver(simpleObserver)
}


private val handlerThread by lazy { HandlerThread("async-post-delay") }

private val workerHandler by lazy { Handler(handlerThread.looper) }

/**
 * Post action in worker thread named 'async-post-delay'
 *
 * Usage of handlerThread : https://www.jianshu.com/p/9c10beaa1c95
 */
fun postDelayAsync(delay: Long, action: Runnable) {
    if (!handlerThread.isAlive) handlerThread.start()
    workerHandler.postDelayed(action, delay)
}


fun postUtilWithTimestamp(condition: Boolean, action: Action, timeout: Long, preTimestamp: Long) {
    if (timeout <= 0) return

    if (condition) {
        action.invoke()
    } else {
        val curTimestamp = System.currentTimeMillis()

        postUtilWithTimestamp(
            condition,
            action,
            timeout = curTimestamp - preTimestamp,
            preTimestamp = curTimestamp
        )
    }
}

fun <T : Handler> T.postUntil(
    condition: Boolean,
    timeout: Long = Long.MAX_VALUE,
    action: Action
): Unit = postUtilWithTimestamp(condition, action, timeout, System.currentTimeMillis())


fun <T : View> T.postUntil(
    condition: Boolean,
    timeout: Long = Long.MAX_VALUE,
    action: Action
): Unit = postUtilWithTimestamp(condition, action, timeout, System.currentTimeMillis())

