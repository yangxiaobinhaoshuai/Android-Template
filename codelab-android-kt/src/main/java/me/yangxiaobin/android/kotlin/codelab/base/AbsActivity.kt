package me.yangxiaobin.android.kotlin.codelab.base

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity

@Suppress("MemberVisibilityCanBePrivate")
abstract class AbsActivity : AppCompatActivity(), LogAbility {

    open override val LogAbility.TAG: String
        get() = "AbsActivity:${this.javaClass.simpleName.take(11)}"

    protected abstract val contentResId: Int

    open val handleBackPress = false

    private val logPrefix by lazy { this.getLogSuffix }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(contentResId)
        logI("$logPrefix onCreate, savedInstanceState :$savedInstanceState")
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
        logI("$logPrefix onStart")
    }

    override fun onRestart() {
        super.onRestart()
        logI("$logPrefix onRestart")
    }

    override fun onResume() {
        super.onResume()
        logI("$logPrefix onResume")
    }

    override fun onPause() {
        super.onPause()
        logI("$logPrefix onPause")
    }

    override fun onStop() {
        super.onStop()
        logI("$logPrefix onStop")
    }

    override fun onDestroy() {
        super.onDestroy()
        logI("$logPrefix onDestroy")
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        logI("$logPrefix onConfigurationChanged, newConfig: $newConfig")
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        logI("$logPrefix onNewIntent, intent: $intent")
    }

    override fun onLowMemory() {
        super.onLowMemory()
        logI("$logPrefix onLowMemory")
    }

    override fun onTrimMemory(level: Int) {
        super.onTrimMemory(level)
        logI("$logPrefix onTrimMemory, level :$level")
    }

    override fun finish() {
        super.finish()
        logI("$logPrefix finish")
    }
}
