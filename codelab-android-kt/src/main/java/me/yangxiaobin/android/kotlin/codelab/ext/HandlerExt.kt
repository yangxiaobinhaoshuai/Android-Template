package me.yangxiaobin.android.kotlin.codelab.ext

import android.os.Handler
import android.os.HandlerThread
import android.os.Looper
import android.os.Message
import android.view.View
import androidx.lifecycle.LifecycleOwner
import me.yangxiaobin.android.kotlin.codelab.lifecycle.SimpleLifecycleObserver
import java.lang.IllegalArgumentException
import java.util.concurrent.Executor
import java.util.concurrent.RejectedExecutionException


val mainLooper: Looper by lazy { Looper.getMainLooper() }
val isMainThread get() = Looper.myLooper() == Looper.getMainLooper()

public val mainHandler: Handler by lazy { Handler(mainLooper) }

val mainExecutor: Executor by lazy { createMainExecutor(mainHandler) }

fun createMainExecutor(mainH: Handler) = Executor { command: Runnable? ->
    if (command != null) {
        if (mainH.post(command)) throw RejectedExecutionException("$mainH is shutting down")
    }
}


fun createMainHandler(handleMessage: (Message) -> Unit): Handler = object : Handler(mainLooper) {
    override fun handleMessage(msg: Message) {
        super.handleMessage(msg)
        handleMessage(msg)
    }
}

/**
 * NB. Can't be mainHandler.
 */
fun Handler.makeCancellable(lifecycleOwner: LifecycleOwner): Handler {

    if (this.looper == mainLooper) throw IllegalArgumentException("Can't make main handler cancellable.")

    val simpleObserver = SimpleLifecycleObserver(
        onBackground = {
            this.removeCallbacksAndMessages(null)
        }
    )

    lifecycleOwner.lifecycle.addObserver(simpleObserver)
    return this
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

fun Handler.postInterval(interval: Long, action: Action) {
    val recursiveAction:Action = {
        action.invoke()
        this.postInterval(interval,action)
    }
    this.postDelayed(recursiveAction,interval)
}


private val handlerThread by lazy { HandlerThread("async-post-delay").apply { this.start() } }

val workerHandler by lazy { Handler(handlerThread.looper) }

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

