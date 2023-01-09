package me.yangxiaobin.android.nav_lib

import androidx.navigation.fragment.findNavController
import me.yangxiaobin.android.kotlin.codelab.base.ability.LogAbility
import me.yangxiaobin.android.kotlin.codelab.log.AndroidLogger
import me.yangxiaobin.common_ui.ButtonsFragment
import me.yangxiaobin.logger.core.LogFacade

class Page1Fragment : ButtonsFragment() {

    override val logger: LogFacade get() = AndroidLogger

    override val LogAbility.TAG: String get() = "NavigationPage1"


    override fun onClick(index: Int) {
        super.onClick(index)
        when (index) {
            0 -> findNavController().navigate(R.id.action_to_page2)
            else -> Unit
        }
    }
}
