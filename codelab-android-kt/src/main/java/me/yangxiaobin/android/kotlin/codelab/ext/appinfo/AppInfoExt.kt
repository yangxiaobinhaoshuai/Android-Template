package me.yangxiaobin.android.kotlin.codelab.ext.appinfo

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.ApplicationInfo
import android.net.wifi.WifiManager
import android.os.Build
import android.provider.Settings
import me.yangxiaobin.android.kotlin.codelab.ext.context.hasPermission
import me.yangxiaobin.android.kotlin.codelab.ext.convertIf
import me.yangxiaobin.android.kotlin.codelab.ext.isNonPositive
import java.io.InputStreamReader
import java.io.LineNumberReader
import java.lang.StringBuilder
import java.net.NetworkInterface
import java.util.*

/**
 * 隐私合规问题，使用以下 API 需要慎重，最好在应用内统一收口
 */

private var sAppName = ""

private var sAppVersionName = ""

private var sAppVersionCode = ""

private var sAndroidId = ""

private var sTargetSdkVersion = 0

private var sMacAddress = ""

public val Context?.getAppName: String
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

public val Context?.getAppVersionName: String
    get() = if (this == null) ""
    else sAppVersionName.convertIf(CharSequence::isEmpty) {

        sAppVersionName = runCatching {
            this.packageManager
                .getPackageInfo(this.packageName, 0)
                .versionName
        }.getOrElse { "" }

        sAppVersionName
    }


public val Context?.getAppVersionCode: String
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

public val Context?.getTargetApi: Int
    get() = if (this == null) 0
    else sTargetSdkVersion.convertIf(Int::isNonPositive) {

        sTargetSdkVersion = runCatching {

            this.applicationInfo?.targetSdkVersion
                ?: this.packageManager.getApplicationInfo(this.packageName, 0).targetSdkVersion

        }.getOrElse { 0 }

        sTargetSdkVersion
    }


public val Context.isDebugBuildType
    get() = try {
        (this.applicationInfo.flags and ApplicationInfo.FLAG_DEBUGGABLE) != 0
    } catch (e: Exception) {
        false
    }

public val Context?.getDisplayDensity: String
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

public val Context?.getAndroidId: String
    @SuppressLint("HardwareIds")
    get() = if (this == null) "" else
        sAndroidId.convertIf(CharSequence::isEmpty) {
            sAndroidId = runCatching {
                Settings.Secure.getString(this.contentResolver, Settings.Secure.ANDROID_ID)
            }.getOrElse { "" }
            sAndroidId
        }


/**
 * Permissions required.
 *
 *     <uses-permission android:name="android.permission.ACCESS_FINE_LOCATION"/>
 *     <uses-permission android:name=" android.permission.LOCAL_MAC_ADDRESS"/>
 */
@SuppressLint("HardwareIds", "MissingPermission")
public fun Context?.getMacAddress(): String {

    if (sMacAddress.isNotEmpty() || this == null || !this.hasPermission(Manifest.permission.ACCESS_WIFI_STATE)) return sMacAddress

    try {
        val wifiManager: WifiManager =
            (this.applicationContext.getSystemService(Context.WIFI_SERVICE) as? WifiManager)
                ?: return ""

        val wifiInfoAddr: String? = wifiManager.connectionInfo?.macAddress

        sMacAddress = wifiInfoAddr ?: ""

        if (sMacAddress.isEmpty() || sMacAddress == "02:00:00:00:00:00") {

            val nets = NetworkInterface.getNetworkInterfaces()
            val interfaces =
                if (nets == null) emptyList<NetworkInterface>() else Collections.list(nets)

            for (networkInterface: NetworkInterface in interfaces) {
                if (!networkInterface.name.equals("wlan0", ignoreCase = true)) {
                    continue
                }

                val macBytes: ByteArray? = networkInterface.hardwareAddress
                if (macBytes == null || macBytes.isEmpty()) break

                val sb = StringBuilder()

                macBytes.forEach { sb.append(String.format("%02X:", it)) }

                if (sb.isNotEmpty()) sb.deleteAt(sb.length - 1)

                sMacAddress = sb.toString()
                break

            }
        }

        if (sMacAddress.isEmpty() || sMacAddress == "02:00:00:00:00:00") {
            val process: Process = Runtime.getRuntime().exec("cat /sys/class/net/wlan0/address ")
            val inputStreamReader = InputStreamReader(process.inputStream)
            val lineNumberReader = LineNumberReader(inputStreamReader)
            val readLine = lineNumberReader.readLine()
            if (!readLine.isNullOrEmpty()) sMacAddress = readLine.uppercase(Locale.US)
        }

    } catch (e: Exception) {
        e.printStackTrace()
    }

    return sMacAddress
}


@SuppressLint("PrivateApi")
public fun Context?.getDNS(): List<String> {

    val servers = mutableSetOf<String>()

    try {
        val sysPropClazz = Class.forName("android.os.SystemProperties")
        val method = sysPropClazz.getMethod("get", String::class.java)
        for (name: String in arrayOf("net.dns1", "net.dns2", "net.dns3", "net.dns4")) {
            val value: String? = method.invoke(null, name) as? String

            if (!value.isNullOrEmpty()) servers.add(value)
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }

    return servers.toList()
}
