package me.yangxiaobin.android.kotlin.codelab.ext.context

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator

/**
 * Permission <uses-permission android:name="android.permission.VIBRATE" /> required
 */
@SuppressLint("MissingPermission")
fun Context.vibrate(milliseconds: Long = 50) {
    val v = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        v.vibrate(VibrationEffect.createOneShot(milliseconds, VibrationEffect.DEFAULT_AMPLITUDE))
    } else {
        v.vibrate(milliseconds)
    }
}
