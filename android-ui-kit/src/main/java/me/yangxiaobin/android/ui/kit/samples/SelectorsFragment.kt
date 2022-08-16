package me.yangxiaobin.android.ui.kit.samples

import android.view.View
import me.yangxiaobin.android.kotlin.codelab.base.LogAbility
import me.yangxiaobin.android.kotlin.codelab.log.AndroidLogger
import me.yangxiaobin.common_ui.EmptyFragment
import me.yangxiaobin.logger.core.LogFacade

class SelectorsFragment : EmptyFragment() {

    override val logger: LogFacade get() = AndroidLogger

    override val LogAbility.TAG: String get() = "SelectorsFragment"

    override fun afterViewCreated(view: View) {
        super.afterViewCreated(view)

    }
}
