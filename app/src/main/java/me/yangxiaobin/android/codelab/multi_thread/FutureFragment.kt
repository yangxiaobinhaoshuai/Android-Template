package me.yangxiaobin.android.codelab.multi_thread

import android.view.View
import me.yangxiaobin.android.codelab.common.ButtonsFragment
import me.yangxiaobin.android.kotlin.codelab.base.LogAbility
import me.yangxiaobin.android.kotlin.codelab.log.AndroidLogger
import me.yangxiaobin.logger.core.LogFacade

class FutureFragment : ButtonsFragment() {

    override val LogAbility.TAG: String get() = "FutureFragment"

    override val logger: LogFacade get() = AndroidLogger

    override fun afterViewCreated(view: View) {
        super.afterViewCreated(view)
    }

    override fun onClick(index: Int) {
        super.onClick(index)
        when (index) {
            0 -> Unit
            1 -> Unit
            2 -> Unit
            3 -> Unit
            4 -> Unit
            5 -> Unit
            else -> Unit
        }
    }
}
