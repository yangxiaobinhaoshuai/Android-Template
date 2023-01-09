package me.yangxiaobin.android.nav_lib

import me.yangxiaobin.android.kotlin.codelab.base.AbsFragment
import me.yangxiaobin.android.kotlin.codelab.base.ability.LogAbility
import me.yangxiaobin.android.kotlin.codelab.log.AndroidLogger
import me.yangxiaobin.logger.core.LogFacade

class NavHostComposeFragment : AbsFragment() {

    override val logger: LogFacade get() = AndroidLogger

    override val LogAbility.TAG: String get() = "NavigationHostCompose"

    override val layoutResId: Int = R.layout.nav_host_fragment

}
