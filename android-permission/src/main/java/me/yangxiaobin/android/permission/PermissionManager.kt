package me.yangxiaobin.android.permission

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import java.lang.IllegalArgumentException
import java.lang.IllegalStateException

object PermissionManager {

    fun within(f: Fragment): PermissionRequest = withContext(f.requireActivity())

    fun within(a: Activity): PermissionRequest = withContext(a)

    fun within(c: Context): PermissionRequest = withContext(c)

    private fun withContext(context: Context): PermissionRequest = when (context) {
        is FragmentActivity -> PermissionRequest(context)
        is ContextWrapper -> withContext(context.baseContext)
        is Activity -> throw IllegalStateException("Can't support Activity, use FragmentActivity instead.")
        else -> throw IllegalArgumentException("Can't convert ${context.javaClass.simpleName} to FragmentActivity.")
    }

}
