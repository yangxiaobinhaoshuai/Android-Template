package me.yangxiaobin.android.kotlin.codelab.base

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import me.yangxiaobin.android.kotlin.codelab.log.L
import me.yangxiaobin.kotlin.codelab.log.logD
import me.yangxiaobin.kotlin.codelab.log.logE
import me.yangxiaobin.kotlin.codelab.log.logI

abstract class AbsActivity : AppCompatActivity() {

    @Suppress("PrivatePropertyName")
    protected val AbsActivity.TAG: String
        get() = "AbsActivity:${this.javaClass.simpleName.take(11)}"

    protected val logI by lazy { L.logI(TAG) }
    protected val logD by lazy { L.logD(TAG) }
    protected val logE by lazy { L.logE(TAG) }

    protected abstract val contentResId: Int

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(contentResId)
        logI("onCreate, savedInstanceState :$savedInstanceState")
        afterOnCreate()
    }

    protected open fun afterOnCreate() {}

    override fun onStart() {
        super.onStart()
        logI("onStart")
    }

    override fun onRestart() {
        super.onRestart()
        logI("onRestart")
    }

    override fun onResume() {
        super.onResume()
        logI("onResume")
    }

    override fun onPause() {
        super.onPause()
        logI("onPause")
    }

    override fun onStop() {
        super.onStop()
        logI("onStop")
    }

    override fun onDestroy() {
        super.onDestroy()
        logI("onDestroy")
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        logI("onConfigurationChanged, newConfig: $newConfig")
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        logI("onNewIntent, intent: $intent")
    }

    override fun onLowMemory() {
        super.onLowMemory()
        logI("onLowMemory")
    }

    override fun onTrimMemory(level: Int) {
        super.onTrimMemory(level)
        logI("onTrimMemory, level :$level")
    }
}
