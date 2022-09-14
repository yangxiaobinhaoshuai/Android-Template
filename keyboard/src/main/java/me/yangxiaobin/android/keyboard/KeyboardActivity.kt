package me.yangxiaobin.android.keyboard

import me.yangxiaobin.android.keyboard.databinding.ActivityKeyboardBinding
import me.yangxiaobin.android.kotlin.codelab.base.AbsViewBindingActivity
import me.yangxiaobin.android.kotlin.codelab.base.LogAbility
import me.yangxiaobin.android.kotlin.codelab.ext.showKeyboardImmediately
import me.yangxiaobin.android.kotlin.codelab.log.AndroidLogger
import me.yangxiaobin.common_ui.EmptyActivity
import me.yangxiaobin.logger.core.LogFacade
import org.jetbrains.anko.intentFor

class KeyboardActivity : AbsViewBindingActivity<ActivityKeyboardBinding>() {


    override val logger: LogFacade get() = AndroidLogger

    override val LogAbility.TAG: String get() = "KeyboardActivity@@"

    private val edt by lazy { binding.edtActivity }
    private val bt by lazy { binding.btActivity }


    override fun afterOnCreate() {
        super.afterOnCreate()
    }

    override fun onResume() {
        super.onResume()
        edt.showKeyboardImmediately()

        bt.setOnClickListener {
            this.startActivity(this.intentFor<EmptyActivity>())
        }
    }


    override fun getActualBinding() = ActivityKeyboardBinding.inflate(this.layoutInflater)

}
