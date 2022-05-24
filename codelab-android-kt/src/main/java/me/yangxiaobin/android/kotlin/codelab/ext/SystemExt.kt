package me.yangxiaobin.android.kotlin.codelab.ext

import android.annotation.SuppressLint
import android.app.ActivityManager
import android.content.Context
import android.os.Build
import android.os.Environment
import android.os.StatFs
import java.io.File


val currentApiLevel get() = Build.VERSION.SDK_INT

fun aboveApi(sdkInt: Int) = currentApiLevel >= sdkInt


fun getRomTotalSpace() = getDirTotalSpace(Environment.getDataDirectory())
fun getRomAvailableSpace() = getDirAvailableSpace(Environment.getDataDirectory())

fun getSdCardTotalSpace() = getDirTotalSpace(Environment.getExternalStorageDirectory())
fun getSdCardAvailableSpace() = getDirAvailableSpace(Environment.getExternalStorageDirectory())


/**
 * 获取某个目录全部空间大小
 */
@SuppressLint("ObsoleteSdkInt")
fun getDirTotalSpace(dir: File): Long = try {
    val statFs = StatFs(dir.path)
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR2) {
        (statFs.blockSize * statFs.blockCount).toLong()
    } else statFs.blockSizeLong * statFs.blockCountLong
} catch (e: Exception) {
    e.printStackTrace()
    0L
}


/**
 * 获取某个目录可用空间大小
 */
@SuppressLint("ObsoleteSdkInt")
fun getDirAvailableSpace(dir: File): Long = try {
    val statFs = StatFs(dir.path)
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR2) {
        (statFs.blockSize * statFs.availableBlocks).toLong()
    } else {
        statFs.blockSizeLong * statFs.availableBlocksLong
    }
} catch (e: Exception) {
    0L
}


/**
 * 获取当前全部内存大小
 */
fun Context?.getTotalMemory(): Long = if (this != null) {
    val am = this.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
    val memoryInfo = ActivityManager.MemoryInfo()
    am.getMemoryInfo(memoryInfo)
    memoryInfo.totalMem
} else 0L

/**
 * 获取当前可用内存大小
 */
fun Context?.getAvailableMemory(): Long = if (this != null) {
    val am = this.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
    val memoryInfo = ActivityManager.MemoryInfo()
    am.getMemoryInfo(memoryInfo)
    memoryInfo.availMem
} else 0L
