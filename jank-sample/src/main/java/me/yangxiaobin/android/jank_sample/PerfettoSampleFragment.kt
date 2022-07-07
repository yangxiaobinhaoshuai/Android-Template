package me.yangxiaobin.android.jank_sample

import me.yangxiaobin.android.kotlin.codelab.base.LogAbility
import me.yangxiaobin.android.kotlin.codelab.log.AndroidLogger
import me.yangxiaobin.common_ui.EmptyFragment
import me.yangxiaobin.logger.core.LogFacade

class PerfettoSampleFragment : EmptyFragment() {

    override val logger: LogFacade get() = AndroidLogger

    override val LogAbility.TAG: String get() = "Perfetto-Sample"

}
