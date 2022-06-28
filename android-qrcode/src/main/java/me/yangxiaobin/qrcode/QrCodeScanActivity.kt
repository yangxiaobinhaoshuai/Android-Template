package me.yangxiaobin.qrcode

import android.view.View
import androidx.fragment.app.FragmentContainerView
import androidx.fragment.app.commit
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import me.yangxiaobin.android.kotlin.codelab.base.AbsActivity
import me.yangxiaobin.android.kotlin.codelab.base.LogAbility
import me.yangxiaobin.android.kotlin.codelab.ext.MatchParentParams
import me.yangxiaobin.android.kotlin.codelab.log.AndroidLogger
import me.yangxiaobin.android.permission.PermissionManager
import me.yangxiaobin.android.permission.PermissionResponse
import me.yangxiaobin.android.permission.requestAsync
import me.yangxiaobin.colors.HexColors
import me.yangxiaobin.colors.colorInt
import me.yangxiaobin.logger.core.LogFacade

/**
 * CameraX 概览 ： https://developer.android.com/training/camerax
 *
 * google cameraX samples : https://github.com/android/camera-samples/
 *
 * cameraX based on camera2, mininal required sdk 21.
 *
 *
 * 1. preview
 * 2. capture
 * 3. imageAnalysis
 *
 * NB. Activity 上的布局会出现在 Fragment 之上
 */
class QrCodeScanActivity : AbsActivity() {

    override val logger: LogFacade get() = AndroidLogger

    override val LogAbility.TAG: String get() = "CameraScanActivity"

    private val rootId = View.generateViewId()

    override fun getRootView(): View {
        return FragmentContainerView(this).apply {
            this.layoutParams = MatchParentParams
            this.setBackgroundColor(HexColors.RED_400.colorInt)
            this.id = rootId
        }
    }

    override fun afterOnCreate() {
        super.afterOnCreate()
        PermissionManager.enableLog(true).setLogTag(TAG)

        //requestSync()
        requestAsync()
    }

    private fun requestSync() {
        PermissionManager
            .createReq(this)
            .request(android.Manifest.permission.CAMERA) {
                onGranted {
                    logD("camera permission is granted : $it.")
                }
                onNeverAskAgain {
                    logD("camera permission never ask again: $it.")
                    PermissionManager.navigateAppDetailSettings(this@QrCodeScanActivity)
                }
            }
    }

    private fun requestAsync() {

        lifecycleScope.launch {

            val res = PermissionManager
                .createReq(this@QrCodeScanActivity)
                .requestAsync(android.Manifest.permission.CAMERA)

            logD("async request permission, res :$res.")

            if (res is PermissionResponse.Success) navigateCameraFragment()
        }

    }

    private fun navigateCameraFragment() {
        supportFragmentManager.commit {
            //addToBackStack(null)
            add(rootId, CameraFragment())
        }
    }

}
