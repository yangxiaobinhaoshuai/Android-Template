package me.yangxiaobin.android.permission

import android.util.Log

data class LogMeta(val tag: String, val message: String)
fun interface PermissionLogFun{
    fun log(meta: LogMeta)
}

data class PermissionReqOption(
    val enableLog: Boolean = false,
    val logTag: String = LOG_TAG,
    val isDebug: Boolean = false,
    val logFun: PermissionLogFun = PermissionLogFun { (t, m) -> Log.d(t, m) },
)
