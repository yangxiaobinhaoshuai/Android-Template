package me.yangxiaobin.android.kotlin.codelab.ext.device

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import me.yangxiaobin.android.kotlin.codelab.ext.context.hasPermission
import me.yangxiaobin.android.kotlin.codelab.ext.nullAsFalse


enum class NetworkType(value: String) {
    NETWORK_TYPE_UNKNOWN("UNKNOWN"),
    NETWORK_TYPE_WIFI("WIFI"),
    NETWORK_TYPE_MOBILE("MOBILE"),
    NETWORK_TYPE_OTHER("OTHER"),
}


/**
 *  Need permission.
 *     <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE"/>
 */
@SuppressLint("MissingPermission")
public fun Context?.getNetworkType(): NetworkType {

    if (!this?.hasPermission(Manifest.permission.ACCESS_NETWORK_STATE).nullAsFalse()) return NetworkType.NETWORK_TYPE_UNKNOWN

    val conn: ConnectivityManager? by lazy { this?.getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager }

    return when {
        this == null -> NetworkType.NETWORK_TYPE_UNKNOWN
        conn != null -> {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                val netCaps: NetworkCapabilities? = conn!!.getNetworkCapabilities(conn!!.activeNetwork)

                when {
                    netCaps == null -> NetworkType.NETWORK_TYPE_UNKNOWN
                    netCaps.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> NetworkType.NETWORK_TYPE_WIFI
                    netCaps.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> NetworkType.NETWORK_TYPE_MOBILE
                    else -> NetworkType.NETWORK_TYPE_OTHER
                }

            } else {

                @Suppress("DEPRECATION")
                when (conn!!.activeNetworkInfo?.type) {
                    ConnectivityManager.TYPE_WIFI -> NetworkType.NETWORK_TYPE_WIFI
                    ConnectivityManager.TYPE_MOBILE -> NetworkType.NETWORK_TYPE_MOBILE
                    else -> NetworkType.NETWORK_TYPE_OTHER
                }
            }


        }
        else -> NetworkType.NETWORK_TYPE_UNKNOWN
    }
}

@SuppressLint("MissingPermission")
public fun Context?.isNetworkConnected(): Boolean {

    val conn: ConnectivityManager? = this?.getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager

    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        conn?.getNetworkCapabilities(conn.activeNetwork)?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) ?: false
    } else {
        @Suppress("DEPRECATION")
        conn?.activeNetworkInfo?.isConnected ?: false
    }
}

public fun Context?.isOnWifi(): Boolean = this?.getNetworkType() == NetworkType.NETWORK_TYPE_WIFI

public fun Context?.isOnMobile(): Boolean = this?.getNetworkType() == NetworkType.NETWORK_TYPE_MOBILE

