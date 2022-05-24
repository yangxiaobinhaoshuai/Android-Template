package me.yangxiaobin.android.permission

import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.commit

typealias OnPermissionResult = (Boolean) -> Unit


class PermissionRequest(private val fragmentActivity: FragmentActivity) {

    /**
     * @see android.Manifest.permission
     */
    fun request(
        permissionString: String,
    ): Diode {

        val diode = Diode(fragmentActivity)

        val alreadyGranted = ActivityCompat.checkSelfPermission(fragmentActivity, permissionString)
        if (alreadyGranted == PackageManager.PERMISSION_GRANTED) return diode

        val manager = fragmentActivity.supportFragmentManager

        val shadowFragment: Fragment = manager.findFragmentByTag(PERMISSION_REQUEST_FRAGMENT_TAG)
            ?: PermissionFragment().also { frag->
                manager.commit(allowStateLoss = true) {
                    add(frag, PERMISSION_REQUEST_FRAGMENT_TAG)
                }
            }

        if (shadowFragment !is PermissionFragment) return diode
        shadowFragment.requestPermission(permissionString, diode)
        return diode
    }


    /**
     * 临时中转类
     */
    class Diode(private val fragmentActivity: FragmentActivity) {

        init {
            logInner("Diode init, hash : ${this.hashCode()}.")
        }

        var isGranted: OnPermissionResult? = null
            private set

        fun onResult(
            isGranted: OnPermissionResult,
        ): PermissionRequest {
            this.isGranted = isGranted
            return PermissionRequest(fragmentActivity)
        }

    }


    companion object {
        const val PERMISSION_REQUEST_FRAGMENT_TAG = "PERMISSION_REQUEST_FRAGMENT_TAG"
    }
}
