package me.yangxiaobin.android.permission

import android.util.Log


private val logEnable by lazy { BuildConfig.DEBUG }


private const val LOG_TAG = "PermissionHelper"


internal fun logInner(message: String) {
    if (!logEnable) return
    Log.d(LOG_TAG, message)
}
