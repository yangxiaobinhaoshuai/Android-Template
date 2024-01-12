package me.yangxiaobin.android.embedding_compat

import android.view.View
import me.yangxiaobin.android.kotlin.codelab.base.ability.LogAbility
import me.yangxiaobin.android.kotlin.codelab.log.AndroidLogger
import me.yangxiaobin.common_ui.EmptyFragment
import me.yangxiaobin.kotlin.codelab.ext.getLimitStacktrace
import me.yangxiaobin.logger.core.LogFacade

class StackTraceFragment : EmptyFragment()  {

    override val logger: LogFacade get() = AndroidLogger

    override val LogAbility.TAG: String get() = "StacktraceFragment"


    override fun afterViewCreated(view: View) {
        super.afterViewCreated(view)
        m1()
    }

    private fun m1(){
        m2()
    }

    private fun m2(){
       logD("getLimitStacktrace(1) :${getLimitStacktrace(1)}")
    }


    override fun onButtonClick(index: Int, v: View) {
        super.onButtonClick(index, v)
        when (index) {
            0 -> {}
            1 -> {}
            else -> {}
        }
    }

}
