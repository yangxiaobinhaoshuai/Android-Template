package me.yangxiaobin.common_ui

import android.view.View
import androidx.core.os.postDelayed
import me.yangxiaobin.android.kotlin.codelab.base.AbsActivity
import me.yangxiaobin.android.kotlin.codelab.base.LogAbility
import me.yangxiaobin.android.kotlin.codelab.ext.mainHandler
import me.yangxiaobin.android.kotlin.codelab.ext.postDelayCancellable
import me.yangxiaobin.android.kotlin.codelab.log.AndroidLogger
import me.yangxiaobin.logger.core.LogFacade

/**
 * 只带有一个 button
 */
open class EmptyActivity : AbsActivity() {


    override val logger: LogFacade get() = AndroidLogger

    override val LogAbility.TAG: String get() = "EmptyActivity####"


    override val contentResId: Int = R.layout.activity_empty_main


    override fun afterOnCreate() {
        super.afterOnCreate()
        init()
    }

    private fun init() {

        mainHandler.postDelayed(5000){
            logD("delay executed.")
        }

        mainHandler.postDelayCancellable(this,5000){
            logD("delay cancelled executed.")
        }

        findViewById<View>(R.id.bt_empty_activity).setOnClickListener {

        }
    }

}
