package me.yangxiaobin.android.kotlin.codelab.base

import android.app.Application
import android.content.Context

open class AbsApplication : Application(), LogAbility {


    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)

    }

    override fun onCreate() {
        super.onCreate()
    }

    override fun onTrimMemory(level: Int) {
        super.onTrimMemory(level)

    }

    override fun onLowMemory() {
        super.onLowMemory()

    }

}
