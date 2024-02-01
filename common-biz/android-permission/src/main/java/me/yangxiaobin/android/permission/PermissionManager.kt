package me.yangxiaobin.android.permission

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import androidx.activity.result.ActivityResult
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity

/**
 * Usage 1:
 *   PermissionManager
 *    .createReq(this)
 *    .request(android.Manifest.permission.CAMERA) {
 *
 *        onGranted {
 *          logD("camera permission is granted : $it.")
 *        }
 *
 *        onNeverAskAgain {
 *          logD("camera permission never ask again: $it.")
 *           PermissionManager.navigateAppDetailSettings(this@QrCodeScanActivity)
 *        }
 *
 *    }
 *
 *    Usage 2:
 *
 *     lifecycleScope.launch {
 *
 *        val res = PermissionManager
 *                .createReq(this@QrCodeScanActivity)
 *                .requestAsync(android.Manifest.permission.CAMERA)
 *
 *          logD("async request permission, res :$res.")
 *
 *         if (res is PermissionResponse.Success) navigateCameraFragment()
 *
 *       }
 *
 */
object PermissionManager {

    var globalCfg = PermissionReqOption()
        private set

    //region config option
    fun configGlobally(cfg: PermissionReqOption): PermissionManager = apply { globalCfg = cfg }
    //endregion


    //region api
    fun createReq(f: Fragment): PermissionRequest = createReqFrom(f.requireActivity())

    fun createReq(a: Activity): PermissionRequest = createReqFrom(a)

    fun createReq(c: Context): PermissionRequest = createReqFrom(c)

    private fun createReqFrom(context: Context): PermissionRequest = when (context) {
        is FragmentActivity -> PermissionRequest(context)
        is Activity -> throw IllegalStateException("Can't support Activity, use FragmentActivity instead.")
        is ContextWrapper -> createReqFrom(context.baseContext)
        else -> throw IllegalArgumentException("Can't convert ${context.javaClass.simpleName} to FragmentActivity.")
    }
    //endregion

    inline fun without(
        context: Context,
        permission: String,
        withoutAction: () -> Unit,
    ) = apply {
        val isDenied = !checkGranted(context, permission)
        if (isDenied) withoutAction.invoke()
    }

    fun nav2SettingAction(context: Context) { if (!jumpToAppDetails(context)) jumpToSysSettings(context) }

    fun checkGranted(context: Context, permission: String): Boolean {
        return ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED
    }

    fun isPermissionNeverAskAgain(activity: Activity, permission: String): Boolean {
        return !checkGranted(activity,permission) && ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)
    }

    internal val permissionInterceptors: MutableMap<String, PermissionInterceptor> = mutableMapOf()
    fun registerPermissionInterceptor(permission: String, interceptor: PermissionInterceptor) =
        apply { permissionInterceptors[permission] = interceptor }

    fun unregisterPermissionInterceptor(permission: String, interceptor: PermissionInterceptor) =
        apply { permissionInterceptors.remove(permission) }

}
fun interface PermissionInterceptor {
    fun intercept(intent: Intent): Boolean
}
