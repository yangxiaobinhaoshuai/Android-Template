package me.yangxiaobin.android.permission

import android.content.Intent
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
        resultBuilder: ResultBuilder = {},
    ) = apply {

        val distinctPermissions: List<String> = permissions.distinct()

        val builder = PermissionResultBuilder()

        try {
            resultBuilder.invoke(builder)
        } catch (e: SecurityException) {
            e.printStackTrace()
            showUsageHint()
            logInner("Have u execute ur permission risk code in 'onGranted' block?")
            return@apply
        }

        val allGranted = distinctPermissions.all { permission-> PermissionManager.checkGranted(fragmentActivity, permission) }
        if (allGranted) {
            logInner("permissionRequest: $distinctPermissions have been granted totally.")
            builder.permissionResult.onGranted?.invoke(distinctPermissions.toTypedArray())
            return@apply
        }

        val manager: FragmentManager = fragmentActivity.supportFragmentManager

        val shadowFragment: Fragment = manager.findFragmentByTag(PERMISSION_REQUEST_FRAGMENT_TAG)
            ?: PermissionFragment().also { frag-> manager.commit(allowStateLoss = true) { add(frag, PERMISSION_REQUEST_FRAGMENT_TAG) } }

        if (shadowFragment !is PermissionFragment) return@apply

        val intercepted: List<String> = distinctPermissions.filter { PermissionManager.permissionInterceptors.keys.contains(it) }
        val unIntercepted: List<String> = distinctPermissions - intercepted.toSet()


        val remained: List<String> = intercepted.filterNot { p ->
            val interceptor = PermissionManager.permissionInterceptors[p] ?: return@filterNot false
            val intent = Intent()
            val shouldIntercept = interceptor.intercept(intent)
            if (shouldIntercept) shadowFragment.requestPermission(p, intent, builder.permissionResult)
            shouldIntercept
        }

        shadowFragment.requestPermission(permissions = (unIntercepted + remained).toTypedArray(), builder.permissionResult)
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
