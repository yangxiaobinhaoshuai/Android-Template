package me.yangxiaobin.android.permission

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings


typealias AndroidPermission = android.Manifest.permission

/**
 * 有操作结果
 */
typealias OnGranted = (Array<String>) -> Unit

/**
 * 不再询问
 */
typealias OnNeverAskAgain = (Array<String>) -> Unit

/**
 * 展示权限理由
 */
typealias OnShouldShowRationale = (Array<String>) -> Unit


/**
 * Guide user granted permission manually.
 *
 * 'QueryPermissionsNeeded' @see https://developer.android.com/training/package-visibility
 */
@SuppressLint("QueryPermissionsNeeded")
fun jumpToAppDetails(context: Context): Boolean {
    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
    val uri: Uri = Uri.fromParts("package", context.packageName, null)
    intent.data = uri

    // NB. https://developer.android.com/training/package-visibility/declaring?hl=zh-cn
    val hasApp = intent.resolveActivity(context.packageManager) != null
    if (hasApp) {
        context.startActivity(intent)
    }
    return hasApp
}

/**
 * Guide user granted permission manually.
 *
 * 'QueryPermissionsNeeded' @see https://developer.android.com/training/package-visibility
 */
@SuppressLint("QueryPermissionsNeeded")
fun jumpToSysSettings(context: Context) {
    val settingIntent = Intent(Settings.ACTION_SETTINGS)
    // NB. https://developer.android.com/training/package-visibility/declaring?hl=zh-cn
    if (settingIntent.resolveActivity(context.packageManager) != null) {
        context.startActivity(settingIntent)
    }
}
