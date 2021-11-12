package me.yangxiaobin.android.kotlin.codelab.lifecycle

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import me.yangxiaobin.android.kotlin.codelab.ext.Action


operator fun Action.plus(other: Action): SimpleLifecycleObserver =
    SimpleLifecycleObserver(this, other)

class SimpleLifecycleObserver(
    private val onForeground: Action,
    private val onBackground: Action,
) : LifecycleObserver {

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun onForeground() = onForeground.invoke()

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun onBackground() = onBackground.invoke()

}
