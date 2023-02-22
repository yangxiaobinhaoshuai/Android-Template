package me.yangxiaobin.android.kotlin.codelab.ext.appinfo

import android.app.Application
import android.content.Context
import android.os.Build
import android.text.TextUtils
import java.io.BufferedReader
import java.io.FileReader


public val Context?.getPkgName: String get() = this?.applicationContext?.packageName ?: ""

public val Context?.isMainProcess: Boolean get() = this?.getPkgName == requireCurrentProcessNameInternal

public val currentProcessName: String get() = requireCurrentProcessNameInternal

public val Context?.getCurrentProcessName: String
    get() = cacheProcessName
        ?: if (this?.isMainProcess == true) "Main($requireCurrentProcessNameInternal)"
        else requireCurrentProcessNameInternal


public val getCurrentPid: Int = android.os.Process.myPid()

private var cacheProcessName: String? = null

private val requireCurrentProcessNameInternal: String
    get() = cacheProcessName
        ?: (if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P)
            Application.getProcessName()
        else getCurProcessNameByCmd())
            .also { cacheProcessName = it }
        ?: throw RuntimeException("Can't get current process name.")

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

