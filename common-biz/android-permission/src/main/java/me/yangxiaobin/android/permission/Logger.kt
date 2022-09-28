package me.yangxiaobin.android.permission

import android.util.Log


internal const val LOG_TAG = "PermissionHelper"

private val logEnable = PermissionManagerOption.enableLog

private val logTag = PermissionManagerOption.logTag


internal fun logInner(message: String) {
    if (!logEnable) return
    Log.d(logTag, message)
}
