package me.yangxiaobin.qrcode

import android.view.View
import me.yangxiaobin.android.kotlin.codelab.base.LogAbility
import me.yangxiaobin.android.kotlin.codelab.log.AndroidLogger
import me.yangxiaobin.common_ui.EmptyActivity
import me.yangxiaobin.logger.core.LogFacade

/**
 * CameraX 概览 ： https://developer.android.com/training/camerax
 */
class QrCodeScanActivity : EmptyActivity() {

    override val logger: LogFacade get() = AndroidLogger

    override val LogAbility.TAG: String get() = "QrCodeScanActivity"


    override fun onButtonClick(v: View) {
        super.onButtonClick(v)
        logD("on Button Clicked.")
    }
}
