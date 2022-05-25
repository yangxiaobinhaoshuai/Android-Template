package me.yangxiaobin.qrcode

import android.view.View
import me.yangxiaobin.android.kotlin.codelab.base.LogAbility
import me.yangxiaobin.android.kotlin.codelab.log.AndroidLogger
import me.yangxiaobin.android.permission.PermissionManager
import me.yangxiaobin.common_ui.EmptyActivity
import me.yangxiaobin.logger.core.LogFacade

/**
 * CameraX 概览 ： https://developer.android.com/training/camerax
 *
 * google cameraX samples : https://github.com/android/camera-samples/
 *
 * cameraX based on camera2, mininal required sdk 21.
 */
class QrCodeScanActivity : EmptyActivity() {

    override val logger: LogFacade get() = AndroidLogger

    override val LogAbility.TAG: String get() = "QrCodeScanActivity"

    override fun afterOnCreate() {
        super.afterOnCreate()
        PermissionManager.createReq(this).request(android.Manifest.permission.CAMERA){
            onResult {
                logD("camera permission is granted : $it.")
            }
        }
    }


    override fun onButtonClick(v: View) {
        super.onButtonClick(v)
        logD("on Button Clicked.")
    }
}
