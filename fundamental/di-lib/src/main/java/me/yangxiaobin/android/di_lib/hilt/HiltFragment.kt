package me.yangxiaobin.android.di_lib.hilt

import android.view.View
import dagger.hilt.android.AndroidEntryPoint
import me.yangxiaobin.android.kotlin.codelab.base.ability.LogAbility
import me.yangxiaobin.android.kotlin.codelab.log.AndroidLogger
import me.yangxiaobin.common_ui.EmptyFragment
import me.yangxiaobin.logger.core.LogFacade

@AndroidEntryPoint
class HiltFragment : EmptyFragment() {

    override val logger: LogFacade get() = AndroidLogger

    override val LogAbility.TAG: String get() = "KtLoopFragment"

    override fun afterViewCreated(view: View) {
        super.afterViewCreated(view)

    }

    override fun onButtonClick(index: Int, v: View) {
        super.onButtonClick(index, v)
        when (index) {
            0 -> {
               NonAndroidComponent().test(requireContext())
            }
            1 -> {
                NonAndroidComponent.staticTest(requireContext())
            }
            else -> {}
        }
    }
}
