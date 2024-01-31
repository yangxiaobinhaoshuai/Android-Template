package me.yangxiaobin.android.permission

import android.os.Looper
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.commit


class PermissionRequest(private val fragmentActivity: FragmentActivity) {

    var reqConfig = PermissionManager.globalCfg
        private set

    fun configReq(config: PermissionReqOption) = apply { reqConfig = config }

    /**
     * @see android.Manifest.permission
     */
    fun request(
        vararg permissions: String,
        resultBuilder: ResultBuilder,
    ) = apply {

        val distinctPermissions = permissions.distinct()

        val builder = PermissionResultBuilder()

        try {
            resultBuilder.invoke(builder)
        } catch (e: SecurityException) {
            e.printStackTrace()
            showUsageHint()
            logInner("Have u execute ur permission risk code in 'onGranted' block?")
            return@apply
        }

        val allGranted = permissions.all { permission-> PermissionManager.checkGranted(fragmentActivity, permission) }
        if (allGranted) {
            logInner("permissionRequest: $distinctPermissions have been granted totally.")
            builder.permissionResult.onGranted?.invoke(distinctPermissions.toTypedArray())
            return@apply
        }

        val manager: FragmentManager = fragmentActivity.supportFragmentManager

        val shadowFragment: Fragment = manager.findFragmentByTag(PERMISSION_REQUEST_FRAGMENT_TAG)
            ?: PermissionFragment().also { frag-> manager.commit(allowStateLoss = true) { add(frag, PERMISSION_REQUEST_FRAGMENT_TAG) } }

        if (shadowFragment !is PermissionFragment) return@apply
        shadowFragment.requestPermission(permissions = permissions, builder.permissionResult)
    }

    private fun showUsageHint() {
        if (reqConfig.isDebug && Looper.myLooper() == Looper.getMainLooper()) {
            Toast.makeText(fragmentActivity, "Have u execute ur permission risk code in 'onGranted' block?", Toast.LENGTH_SHORT).show()
        }
    }



    companion object {
        const val PERMISSION_REQUEST_FRAGMENT_TAG = "PERMISSION_REQUEST_FRAGMENT_TAG"
    }
}
