package me.yangxiaobin.android.permission

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity


object PermissionManager {

    //region config option
    fun enableLog(enable: Boolean): PermissionManagerOption = PermissionManagerOption.enableLog(enable)

    fun setLogTag(tag: String): PermissionManagerOption = PermissionManagerOption.setLogTag(tag)
    //endregion


    //region api
    fun createReq(f: Fragment): PermissionRequest = createReqFrom(f.requireActivity())

    fun createReq(a: Activity): PermissionRequest = createReqFrom(a)

    fun createReq(c: Context): PermissionRequest = createReqFrom(c)

    private fun createReqFrom(context: Context): PermissionRequest = when (context) {
        is FragmentActivity -> PermissionRequest(context)
        is ContextWrapper -> createReqFrom(context.baseContext)
        is Activity -> throw IllegalStateException("Can't support Activity, use FragmentActivity instead.")
        else -> throw IllegalArgumentException("Can't convert ${context.javaClass.simpleName} to FragmentActivity.")
    }
    //endregion

    /**
     * Guide user granted permission manually.
     *
     * 'QueryPermissionsNeeded' @see https://developer.android.com/training/package-visibility
     */
    @SuppressLint("QueryPermissionsNeeded")
    fun navigateAppDetailSettings(context: Context) {
        var settingIntent = Intent("android.settings.APPLICATION_DETAILS_SETTINGS")
        settingIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        settingIntent.data = Uri.fromParts("package", context.packageName, null)
        if (settingIntent.resolveActivity(context.packageManager) != null) {
            context.startActivity(settingIntent)
        } else {
            settingIntent = Intent(Settings.ACTION_SETTINGS)
            if (settingIntent.resolveActivity(context.packageManager) != null) {
                context.startActivity(settingIntent)
            }
        }
    }


}
