package me.yangxiaobin.android.kotlin.codelab.ext

import android.content.Context
import android.content.pm.PackageManager
import android.content.pm.Signature
import android.util.Base64
import java.security.MessageDigest


fun getApplicationSHA256(context: Context): String {
    try {
        val packageName = context.packageName
        val packageManager = context.packageManager

        val signatures: Array<Signature> = packageManager.getPackageInfo(packageName, PackageManager.GET_SIGNATURES).signatures

        val md = MessageDigest.getInstance("SHA-256")
        md.update(signatures[0].toByteArray())

        val signatureHash = md.digest()
        return Base64.encodeToString(signatureHash, Base64.DEFAULT)
    } catch (e: Exception) {
        e.printStackTrace()
    }

    return ""
}

/**
 *     <uses-permission android:name="android.permission.QUERY_ALL_PACKAGES"/>
 */
fun getApplicationSHA1(context: Context, pkgName: String? = null): String {
    try {
        val packageName = pkgName ?: context.packageName
        val packageManager = context.packageManager

        val signatures: Array<Signature> = packageManager.getPackageInfo(packageName, PackageManager.GET_SIGNATURES).signatures
        val md = MessageDigest.getInstance("SHA-1")

        for (signature in signatures) {
            md.update(signature.toByteArray())
        }

        val sha1Hash = md.digest()
        val sha1Result = StringBuilder()

        for (byte in sha1Hash) {
            sha1Result.append(String.format("%02X", byte))
        }

        return sha1Result.toString()
    } catch (e: Exception) {
        e.printStackTrace()
    }

    return ""
}

