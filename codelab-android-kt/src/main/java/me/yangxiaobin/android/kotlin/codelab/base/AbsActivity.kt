package me.yangxiaobin.android.kotlin.codelab.base

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity

abstract class AbsActivity : AppCompatActivity(), LogAbility {

    override val LogAbility.TAG: String
        get() = "AbsActivity:${this.javaClass.simpleName.take(11)}"

    protected abstract val contentResId: Int

    open val handleBackPress = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(contentResId)
        logI("onCreate, savedInstanceState :$savedInstanceState")
        registerBackHandler()
        afterOnCreate()

    }

    private fun registerBackHandler() = this.onBackPressedDispatcher.addCallback(this,
        object : OnBackPressedCallback(handleBackPress) {
            override fun handleOnBackPressed() {
                onHandleBackPress()
            }

        })

    protected open fun afterOnCreate() {}

    protected open fun onHandleBackPress() {}

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

    override fun finish() {
        super.finish()
        logI("finish")
    }
}
