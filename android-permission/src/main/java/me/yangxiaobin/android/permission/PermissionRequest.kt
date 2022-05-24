package me.yangxiaobin.android.permission

import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.commit


class PermissionRequest(private val fragmentActivity: FragmentActivity) {

    /**
     * @see android.Manifest.permission
     */
    fun request(
        permissionString: String,
        resultBuilder: ResultBuilder,
    ) = apply {

        val alreadyGranted = ActivityCompat.checkSelfPermission(fragmentActivity, permissionString)
        if (alreadyGranted == PackageManager.PERMISSION_GRANTED) return@apply

        val manager = fragmentActivity.supportFragmentManager

        val shadowFragment: Fragment = manager.findFragmentByTag(PERMISSION_REQUEST_FRAGMENT_TAG)
            ?: PermissionFragment().also { frag->
                manager.commit(allowStateLoss = true) {
                    add(frag, PERMISSION_REQUEST_FRAGMENT_TAG)
                }
            }

        if (shadowFragment !is PermissionFragment) return@apply

        val builder = PermissionResultBuilder()
        resultBuilder.invoke(builder)

        shadowFragment.requestPermission(permissionString, builder.permissionResult)
    }



    companion object {
        const val PERMISSION_REQUEST_FRAGMENT_TAG = "PERMISSION_REQUEST_FRAGMENT_TAG"
    }
}
