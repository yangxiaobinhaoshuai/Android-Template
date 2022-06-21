package me.yangxiaobin.android.kotlin.codelab.ext

import android.os.Handler
import android.os.HandlerThread
import android.os.Looper
import android.os.Message
import androidx.core.os.postDelayed
import androidx.lifecycle.LifecycleOwner
import me.yangxiaobin.android.kotlin.codelab.lifecycle.SimpleLifecycleObserver


val mainLopper: Looper get() = Looper.getMainLooper()

val mainHandler: Handler get() = Handler(mainLopper)

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
