package me.yangxiaobin.android.codelab

import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.ProcessLifecycleOwner
import me.yangxiaobin.android.kotlin.codelab.base.AbsApplication

class MyApp : AbsApplication() {

    override fun onCreate() {
        super.onCreate()
        ProcessLifecycleOwner.get().lifecycle.addObserver(object : LifecycleObserver{

        })
    }

}
