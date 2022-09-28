package me.yangxiaobin.android.permission

object PermissionManagerOption {

    var enableLog = false
        private set

    var logTag = LOG_TAG
        private set

    fun enableLog(enable: Boolean) = apply { enableLog = enable }

    fun setLogTag(tag: String) = apply { logTag = tag }
}
