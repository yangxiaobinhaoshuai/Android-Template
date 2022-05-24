package me.yangxiaobin.android.kotlin.codelab.ext

import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.ApplicationInfo
import android.os.Build
import android.provider.Settings

private var sAppName = ""

private var sAppVersionName = ""

private var sAppVersionCode = ""

private var sAndroidId = ""

val Context?.getAppName: String
    get() = if (this == null) ""
    else sAppName.ifEmpty {
        sAppName = try {
            this.packageManager
                .getPackageInfo(this.packageName, 0)
                .applicationInfo.loadLabel(this.packageManager)
                .toString()
        } catch (e: Exception) {
            ""
        }
        sAppName
    }

val Context?.getAppVersionName: String
    get() = if (this == null) ""
    else sAppVersionName.convertIf(CharSequence::isEmpty) {

        sAppVersionName = runCatching {
            this.packageManager
                .getPackageInfo(this.packageName, 0)
                .versionName
        }.getOrElse { "" }

        sAppVersionName
    }


val Context?.getAppVersionCode: String
    get() = if (this == null) ""
    else sAppVersionCode.convertIf(CharSequence::isEmpty) {

        val versionCode: Long = try {
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.P) {
                this.packageManager
                    .getPackageInfo(this.packageName, 0)
                    .longVersionCode
            } else {
                @Suppress("DEPRECATION")
                this.packageManager
                    .getPackageInfo(this.packageName, 0)
                    .versionCode
                    .toLong()
            }
        } catch (e: Exception) {
            0
        }

        sAppVersionCode = versionCode.toString()
        sAppVersionCode
    }

val Context.isDebugBuildType
    get() = try {
        (this.applicationInfo.flags and ApplicationInfo.FLAG_DEBUGGABLE) != 0
    } catch (e: Exception) {
        false
    }

val Context?.getDisplayDensity: String
    get() = if (this == null) ""
    else {
        val density = this.resources.displayMetrics.density
        when {
            density <= 1 -> "mdpi"
            density < 2 -> "hdpi"
            density < 2.5 -> "xhdpi"
            density <= 3 -> "xxhdpi"
            else -> "xxxhdpi"
        }
    }

val Context?.getAndroidId: String
    @SuppressLint("HardwareIds")
    get() = if (this == null) "" else
        sAndroidId.convertIf(CharSequence::isEmpty) {
            sAndroidId = runCatching {
                Settings.Secure.getString(this.contentResolver, Settings.Secure.ANDROID_ID)
            }.getOrElse { "" }
            sAndroidId
        }
