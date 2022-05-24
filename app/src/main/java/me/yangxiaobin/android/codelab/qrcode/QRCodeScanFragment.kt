package me.yangxiaobin.android.codelab.qrcode

import android.content.pm.PackageManager
import android.view.View
import androidx.core.app.ActivityCompat
import me.yangxiaobin.android.codelab.common.ButtonsFragment
import me.yangxiaobin.android.kotlin.codelab.base.LogAbility
import me.yangxiaobin.android.kotlin.codelab.log.AndroidLogger
import me.yangxiaobin.logger.clone
import me.yangxiaobin.logger.core.LogFacade
import me.yangxiaobin.logger.core.LogLevel

class QRCodeScanFragment : ButtonsFragment() {

    override val logger: LogFacade get() = AndroidLogger.clone(logLevel = LogLevel.DEBUG)

    override val LogAbility.TAG: String get() = "PermissionFragment"


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
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(android.Manifest.permission.CAMERA),
                1234)
        } else logD("has camera permission.")

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

}
