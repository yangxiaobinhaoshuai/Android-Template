package me.yangxiaobin.android.kotlin.codelab.base

import android.app.Activity
import android.app.Application
import android.content.Context
import android.os.Bundle
import me.yangxiaobin.android.kotlin.codelab.base.ability.LogAbility
import me.yangxiaobin.android.kotlin.codelab.base.manager.ActivityLifecycleAdapter
import me.yangxiaobin.android.kotlin.codelab.base.manager.ActivityRecorder

open class AbsApplication : Application(), LogAbility {

    override val LogAbility.TAG: String get() = "AbsApp:${this.javaClass.simpleName.take(11)}"

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        logI("attachBaseContext,base:$base.")
    }

    override fun onCreate() {
        super.onCreate()
        logI("onCreate.")

        this.registerActivityLifecycleCallbacks(object : ActivityLifecycleAdapter() {

            override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
                super.onActivityCreated(activity, savedInstanceState)
                ActivityRecorder.add(activity)
            }

            override fun onActivityDestroyed(activity: Activity) {
                super.onActivityDestroyed(activity)
                ActivityRecorder.remove(activity)
            }

        })
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
