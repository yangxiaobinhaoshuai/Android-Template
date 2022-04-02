package me.yangxiaobin.android.kotlin.codelab.base

import android.app.Application
import android.content.Context

open class AbsApplication : Application(), LogAbility {

    override val LogAbility.TAG: String
        get() = "AbsApp:${this.javaClass.simpleName.take(11)}"

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        logI("attachBaseContext,base:$base.")
    }

    override fun onCreate() {
        super.onCreate()
        logI("onCreate.")
    }

    override fun onTrimMemory(level: Int) {
        super.onTrimMemory(level)
        logI("onTrimMemory.")
    }

    override fun onLowMemory() {
        super.onLowMemory()
        logI("onLowMemory.")
    }

}
