package me.yangxiaobin.android.kotlin.codelab.base

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import me.yangxiaobin.android.kotlin.codelab.base.ability.LogAbility
import me.yangxiaobin.android.kotlin.codelab.base.ability.ResAbility
import me.yangxiaobin.android.kotlin.codelab.ext.context.buildResColor
import me.yangxiaobin.android.kotlin.codelab.ext.context.buildResDrawable
import me.yangxiaobin.android.kotlin.codelab.ext.context.buildResString
import me.yangxiaobin.android.kotlin.codelab.ext.uicontroller.setActionBarTitle
import me.yangxiaobin.kotlin.codelab.ext.neatName

abstract class AbsActivity : AppCompatActivity(), LogAbility, ResAbility {

    override val LogAbility.TAG: String get() = "AbsActivity:${this.neatName.take(11)}"

    protected open val contentResId: Int = -1

    open val handleBackPress = false

    override val asColor by lazy { buildResColor(this) }
    override val asString by lazy { buildResString(this) }
    override val asDrawable by lazy { buildResDrawable(this) }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        beforeOnCreate()
        dispatchSetContent(contentResId)
        logI("onCreate, savedInstanceState :$savedInstanceState.")
        registerBackHandler()
        afterOnCreate()
    }

    private fun registerBackHandler() = this.onBackPressedDispatcher
        .addCallback(
            this,
            object : OnBackPressedCallback(handleBackPress) {
                override fun handleOnBackPressed() {
                    onHandleBackPress()
                }
            })

    private fun dispatchSetContent(contentResId: Int) {
        if (contentResId > 0) setContentView(contentResId)
        else setContentView(getRootView())
    }

    protected open fun getRootView(): View = View(this).apply { this.setBackgroundColor(android.R.color.holo_blue_light.asColor()) }

    protected open fun beforeOnCreate() {}

    protected open fun afterOnCreate() {
        this.setActionBarTitle(TAG)
    }

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
