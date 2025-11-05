package com.yangxiaobin.route_sample

import android.content.Intent
import android.view.View
import me.yangxiaobin.android.kotlin.codelab.base.ability.LogAbility
import me.yangxiaobin.android.kotlin.codelab.ext.intentFor
import me.yangxiaobin.android.kotlin.codelab.log.AndroidLogger
import me.yangxiaobin.colors.HexColors
import me.yangxiaobin.colors.colorInt
import me.yangxiaobin.common_ui.ButtonsFragment
import me.yangxiaobin.logger.core.LogFacade

class RouterFragment : ButtonsFragment() {

    override val logger: LogFacade get() = AndroidLogger

    override val LogAbility.TAG: String get() = "RouterFragment"


    override fun afterViewCreated(view: View) {
        super.afterViewCreated(view)
        view.setBackgroundColor(HexColors.PINK_400.colorInt)
    }

    override fun onClick(index: Int) {
        super.onClick(index)
        when (index) {
            0 -> requireContext().startActivity(requireContext().intentFor<RouterActivity>())
            1 -> {

                val intent = Intent()
                this.startActivityForResult(intent, 0)
            }
            else -> Unit
        }
    }

}
