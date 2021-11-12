package me.yangxiaobin.android.kotlin.codelab.base

import android.app.Application
import android.content.Context

open class AbsApplication : Application(), LogAbility {

    override val LogAbility.TAG: String
        get() = "AbsApp:${this.javaClass.simpleName.take(11)}"

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        logI("attachBaseContext,base:$base, ${base?.getLogSuffix}")
    }

    override fun onCreate() {
        super.onCreate()
        logI("onCreate, ${this.getLogSuffix}")
    }

    override fun onTrimMemory(level: Int) {
        super.onTrimMemory(level)
        logI("onTrimMemory, ${this.getLogSuffix}")
    }

    override fun onLowMemory() {
        super.onLowMemory()
        logI("onLowMemory, ${this.getLogSuffix}")
    }

}
