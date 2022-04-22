package me.yangxiaobin.android.kotlin.codelab.lifecycle

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import me.yangxiaobin.android.kotlin.codelab.ext.Action
import me.yangxiaobin.android.kotlin.codelab.ext.emptyAction


class SimpleLifecycleObserver(
    private val onForeground: Action = emptyAction,
    private val onBackground: Action = emptyAction,
) : LifecycleObserver {

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun onForeground() = onForeground.invoke()

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun onBackground() = onBackground.invoke()

}
