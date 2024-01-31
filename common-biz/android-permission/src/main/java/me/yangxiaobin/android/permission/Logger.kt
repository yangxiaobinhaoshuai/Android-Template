package me.yangxiaobin.android.permission

import android.util.Log


internal const val LOG_TAG = "PermissionHelper"

private val logEnable by lazy { PermissionManager.globalCfg.enableLog }

private val logTag by lazy { PermissionManager.globalCfg.logTag }


internal fun logInner(message: String) {
    if (!logEnable) return
    PermissionManager.globalCfg.logFun.log(LogMeta(logTag, message))
}

internal fun PermissionRequest.logInnerReq(message: String) {
    if (!logEnable) return
    this.reqConfig.logFun.log(LogMeta(logTag, message))
}
