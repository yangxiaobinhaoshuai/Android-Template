package me.yangxiaobin.android.permission

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import java.lang.IllegalArgumentException
import java.lang.IllegalStateException

object PermissionManager {

    fun createReq(f: Fragment): PermissionRequest = createReqFrom(f.requireActivity())

    fun createReq(a: Activity): PermissionRequest = createReqFrom(a)

    fun createReq(c: Context): PermissionRequest = createReqFrom(c)

    private fun createReqFrom(context: Context): PermissionRequest = when (context) {
        is FragmentActivity -> PermissionRequest(context)
        is ContextWrapper -> createReqFrom(context.baseContext)
        is Activity -> throw IllegalStateException("Can't support Activity, use FragmentActivity instead.")
        else -> throw IllegalArgumentException("Can't convert ${context.javaClass.simpleName} to FragmentActivity.")
    }

}
