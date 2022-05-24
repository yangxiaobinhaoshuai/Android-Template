package me.yangxiaobin.android.permission

import android.app.Activity
import androidx.fragment.app.Fragment

object PermissionManager {

    fun withFragment(f: Fragment) = apply {

    }

    fun withActivity(a: Activity) = apply {

    }

    /**
     * @see android.Manifest.permission
     */
    fun request(vararg permissionString: String) {

    }

    suspend fun requestAync(vararg permissionString: String) {

    }


}
