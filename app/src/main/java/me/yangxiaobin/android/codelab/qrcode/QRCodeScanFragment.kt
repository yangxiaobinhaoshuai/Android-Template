package me.yangxiaobin.android.codelab.qrcode

import android.content.pm.PackageManager
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import me.yangxiaobin.android.codelab.common.ButtonsFragment
import me.yangxiaobin.android.kotlin.codelab.base.LogAbility
import me.yangxiaobin.android.kotlin.codelab.log.AndroidLogger
import me.yangxiaobin.android.permission.PermissionManager
import me.yangxiaobin.logger.clone
import me.yangxiaobin.logger.core.LogFacade
import me.yangxiaobin.logger.core.LogLevel

class QRCodeScanFragment : ButtonsFragment() {

    override val logger: LogFacade get() = AndroidLogger.clone(logLevel = LogLevel.DEBUG)

    override val LogAbility.TAG: String get() = "PermissionFragment"


    private val launcher = registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            logD("registerForActivityResult, isGranted :$isGranted.")
        }

    override fun afterViewCreated(view: View) {
        super.afterViewCreated(view)
        requestPermissionInner()
    }

    /**
     *  Need manifest permission declaration too.
     *
     *   <uses-permission android:name="android.permission.CAMERA"/>
     */
    private fun requestPermissionInner() {
        val res: Int =
            ActivityCompat.checkSelfPermission(requireContext(), android.Manifest.permission.CAMERA)
        if (res == PackageManager.PERMISSION_DENIED) {
            logD("Does NOT has camera permission, so request it.")
        } else logD("has camera permission.")


        PermissionManager.createReq(this).request(android.Manifest.permission.CAMERA) {

            onResult { logD("onResult : $it.") }
            shouldShowRationale { logD("shouldShowRationale : $it.") }
            onNeverAskAgain { logD("onNeverAskAgain : $it.") }
        }

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray,
    ) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        logD("""
            onRequestPermissionsResult -> 
            requestCode : $requestCode
            permissions : $permissions
            grantResults : $grantResults
        """.trimIndent())
    }

    override fun onClick(index: Int) {
        super.onClick(index)
        when (index) {
            0 -> {
                PermissionManager.createReq(this).request(android.Manifest.permission.CAMERA) {

                    onResult { logD("click onResult : $it.") }
                    shouldShowRationale { logD("click shouldShowRationale : $it.") }
                    onNeverAskAgain { logD("click onNeverAskAgain : $it.") }
                }
            }

            1 -> {
                launcher.launch(android.Manifest.permission.CAMERA)
            }

            else -> {
            }
        }
    }

}
