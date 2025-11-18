package me.yangxiaobin.android.codelab.jepack_compose

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry

class ResumeLifecycleOwner : LifecycleOwner {
    private val mLifecycleRegistry = LifecycleRegistry(this)

    fun handleLifecycleEvent(event: Lifecycle.Event) {
        mLifecycleRegistry.handleLifecycleEvent(event)
    }

    override val lifecycle: Lifecycle = mLifecycleRegistry
}
