package me.yangxiaobin.android.keyboard

import android.content.Context
import android.view.KeyEvent
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import me.yangxiaobin.android.keyboard.databinding.ActivityKeyboardBinding
import me.yangxiaobin.android.kotlin.codelab.base.AbsViewBindingActivity
import me.yangxiaobin.android.kotlin.codelab.base.ability.LogAbility
import me.yangxiaobin.android.kotlin.codelab.log.AndroidLogger
import me.yangxiaobin.kotlin.codelab.ext.neatName
import me.yangxiaobin.logger.core.LogFacade

class KeyboardActivity : AbsViewBindingActivity<ActivityKeyboardBinding>() {


    override val logger: LogFacade get() = AndroidLogger

    override val LogAbility.TAG: String get() = "KeyboardActivity@@"

    private val edt: EditText by lazy { binding.edtActivity }
    private val bt1 by lazy { binding.bt1Activity }
    private val bt2 by lazy { binding.bt2Activity }
    private val bt3 by lazy { binding.bt3Activity }

    // 禁用动画效果
    private val softInputAdjustNothing = WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING

    // A 有键盘弹出，B 无键盘弹出， A -> B ，B 返回 A 后键盘弹出
    private val inputAlwaysVisible = WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE


    override fun beforeOnCreate() {
        super.beforeOnCreate()

        window.attributes.windowAnimations = 0

        /**
         * Soft mode
         * @see https://www.jianshu.com/p/3d5d5d60d336
         */

        // 无效
        //val softMode = WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE

        // 无效
        //val softMode = WindowManager.LayoutParams.SOFT_INPUT_STATE_UNCHANGED


        window.setSoftInputMode(inputAlwaysVisible or softInputAdjustNothing)
    }

    override fun afterOnCreate() {
        super.afterOnCreate()
    }

    override fun onResume() {
        super.onResume()
        //edt.showKeyboardImmediately()


        bt1.setOnClickListener {

            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(edt.windowToken, 0)

            //this.startActivity(this.intentFor<EmptyActivity>())
        }

        bt2.setOnClickListener {
            window.setSoftInputMode(inputAlwaysVisible)
        }

        bt3.setOnClickListener {
            window.setSoftInputMode(softInputAdjustNothing)
        }
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
