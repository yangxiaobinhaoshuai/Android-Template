package me.yangxiaobin.android.kotlin.codelab.ext

import android.app.Application
import android.content.Context
import android.os.Build
import android.text.TextUtils
import java.io.BufferedReader
import java.io.FileReader


val Context.getPkgName: String get() = this.applicationContext.packageName

val Context.isMainProcess: Boolean get() = this.getPkgName == getCurrentProcessNameInternal

val Context.getCurrentProcessName: String
    get() = if (this.isMainProcess) "Main($getCurrentProcessNameInternal)"
    else requireCurrentProcessName


val getCurrentPid = android.os.Process.myPid()

val requireCurrentProcessName: String =
    getCurrentProcessNameInternal ?: throw RuntimeException("Can't get current process name.")

val getCurrentProcessName: String?
    get() = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
        Application.getProcessName();
    } else {
        getCurProcessNameByCmd()
    }


private val getCurrentProcessNameInternal: String?
    get() = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
        Application.getProcessName()
    } else {
        getCurProcessNameByCmd()
    }

private fun getCurProcessNameByCmd(): String? =
    try {
        val reader = BufferedReader(FileReader("/proc/$getCurrentPid/cmdline"))
        var processName = reader.readLine()
        if (!TextUtils.isEmpty(processName)) {
            processName = processName.trim { it <= ' ' }
        }
        processName
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }

