package me.yangxiaobin.qrcode

import android.view.View
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import me.yangxiaobin.android.kotlin.codelab.base.LogAbility
import me.yangxiaobin.android.kotlin.codelab.log.AndroidLogger
import me.yangxiaobin.android.permission.PermissionManager
import me.yangxiaobin.android.permission.requestAsync
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

    override val LogAbility.TAG: String get() = "QrCodeScan##"

    override fun afterOnCreate() {
        super.afterOnCreate()
        PermissionManager.enableLog(true).setLogTag(TAG)

        //requestSync()
        requestAsync()
    }

    private fun requestSync(){
        PermissionManager
            .createReq(this)
            .request(android.Manifest.permission.CAMERA) {
                onResult {
                    logD("camera permission is granted : $it.")
                }
                onNeverAskAgain {
                    logD("camera permission never ask again: $it.")
                    PermissionManager.navigateAppDetailSettings(this@QrCodeScanActivity)
                }
            }
    }

    private fun requestAsync(){

        lifecycleScope.launch{

            val res = PermissionManager
                .createReq(this@QrCodeScanActivity)
                .requestAsync(android.Manifest.permission.CAMERA)

            logD("async request permission, res :$res.")
        }

    }


    override fun onButtonClick(v: View) {
        super.onButtonClick(v)
        logD("on Button Clicked.")
    }
}
