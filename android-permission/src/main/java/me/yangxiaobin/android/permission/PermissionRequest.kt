package me.yangxiaobin.android.permission

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.commit


class PermissionRequest(private val fragmentActivity: FragmentActivity) {

    private var enableLog = false
    private var logTag = LOG_TAG

    fun enableLog(enable: Boolean, logTag: String = LOG_TAG) = apply {
        enableLog = enable
        this.logTag = logTag
    }

    /**
     * @see android.Manifest.permission
     */
    fun request(
        vararg permissions: String,
        resultBuilder: ResultBuilder,
    ) = apply {

        val builder = PermissionResultBuilder()
        resultBuilder.invoke(builder)

        val allGranted = permissions.all { permission-> PermissionManager.checkGranted(fragmentActivity, permission) }
        if (allGranted) {
            logInner("permissionRequest: $permissions have been granted totally.")
            builder.permissionResult.onGranted?.invoke(arrayOf(*permissions))
            return@apply
        }

        val manager = fragmentActivity.supportFragmentManager

        val shadowFragment: Fragment = manager.findFragmentByTag(PERMISSION_REQUEST_FRAGMENT_TAG)
            ?: PermissionFragment().also { frag->
                manager.commit(allowStateLoss = true) {
                    add(frag, PERMISSION_REQUEST_FRAGMENT_TAG)
                }
            }

        if (shadowFragment !is PermissionFragment) return@apply
        shadowFragment.requestPermission(permissions = permissions, builder.permissionResult)
    }



    companion object {
        const val PERMISSION_REQUEST_FRAGMENT_TAG = "PERMISSION_REQUEST_FRAGMENT_TAG"
    }
}
