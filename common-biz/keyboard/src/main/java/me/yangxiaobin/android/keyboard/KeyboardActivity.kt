package me.yangxiaobin.android.keyboard

import android.view.KeyEvent
import android.view.WindowManager
import me.yangxiaobin.android.keyboard.databinding.ActivityKeyboardBinding
import me.yangxiaobin.android.kotlin.codelab.base.AbsViewBindingActivity
import me.yangxiaobin.android.kotlin.codelab.base.ability.LogAbility
import me.yangxiaobin.android.kotlin.codelab.log.AndroidLogger
import me.yangxiaobin.common_ui.EmptyActivity
import me.yangxiaobin.kotlin.codelab.ext.neatName
import me.yangxiaobin.logger.core.LogFacade
import org.jetbrains.anko.intentFor

class KeyboardActivity : AbsViewBindingActivity<ActivityKeyboardBinding>() {


    override val logger: LogFacade get() = AndroidLogger

    override val LogAbility.TAG: String get() = "KeyboardActivity@@"

    private val edt by lazy { binding.edtActivity }
    private val bt by lazy { binding.btActivity }


    override fun beforeOnCreate() {
        super.beforeOnCreate()

        /**
         * Soft mode
         * @see https://www.jianshu.com/p/3d5d5d60d336
         */

        // 无效
        //val softMode = WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE

        // 无效
        //val softMode = WindowManager.LayoutParams.SOFT_INPUT_STATE_UNCHANGED

        // A 有键盘弹出，B 无键盘弹出， A -> B ，B 返回 A 后键盘弹出
        val softMode = WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE
        window.setSoftInputMode(softMode)
    }

    override fun afterOnCreate() {
        super.afterOnCreate()
    }

    override fun onResume() {
        super.onResume()
        //edt.showKeyboardImmediately()


        bt.setOnClickListener { this.startActivity(this.intentFor<EmptyActivity>()) }
//        bt.setOnClickListener {
//            val c = this.findViewById<View>(android.R.id.closeButton)
//            toast("c :$c")
//        }
    }


    override fun getActualBinding() = ActivityKeyboardBinding.inflate(this.layoutInflater)


    override val handleBackPress: Boolean get() = true

    override fun onHandleBackPress() {
        super.onHandleBackPress()
        logI("$neatName onHandleBackPress.")
        finish()
    }


    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        logI("onKeyDown, code: $keyCode, event action :${event.action}.")
        return super.onKeyDown(keyCode, event)
    }
}
