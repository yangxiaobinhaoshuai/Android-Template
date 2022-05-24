package me.yangxiaobin.android.permission

import android.util.Log


private val logEnable by lazy { BuildConfig.DEBUG }

// TODO
private const val LOG_TAG = "PermissionFragment"
//private const val LOG_TAG = "PermissionFragment"


internal fun logInner(message: String) {
    if (!logEnable) return
    Log.d(LOG_TAG, message)
}
