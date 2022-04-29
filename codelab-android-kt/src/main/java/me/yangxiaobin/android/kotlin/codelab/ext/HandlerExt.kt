package me.yangxiaobin.android.kotlin.codelab.ext

import android.os.Handler
import android.os.Looper
import android.os.Message
import androidx.core.os.postDelayed
import androidx.lifecycle.LifecycleOwner
import me.yangxiaobin.android.kotlin.codelab.lifecycle.SimpleLifecycleObserver


val mainLopper: Looper get() = Looper.getMainLooper()

val mainHandler: Handler get() = Handler(mainLopper)

fun getMainHandler(handleMessage: (Message) -> Unit): Handler = object : Handler(mainLopper) {
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
