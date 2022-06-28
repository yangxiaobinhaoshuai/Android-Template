package me.yangxiaobin.android.permission

import android.os.Bundle
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment

/**
 *  请求应用权限: https://developer.android.com/training/permissions/requesting
 *
 *
 *  shouldShowRationale & neverAskAgain see : https://stackoverflow.com/a/34612503/10247834
 */
internal class PermissionFragment : Fragment() {


    private lateinit var permissions: Array<String>
    private lateinit var result: PermissionResult

    init {
        logInner("PermissionFragment init ,hash : ${this.hashCode()}.")
    }


    // Register the permissions callback, which handles the user's response to the
    // system permissions dialog. Save the return value, an instance of
    // ActivityResultLauncher. You can use either a val, as shown in this snippet,
    // or a lateinit var in your onAttach() or onCreate() method.
    private val requestPermissionLauncher: ActivityResultLauncher<Array<String>> =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { resMap: MutableMap<String, Boolean> ->

            logInner("permission request launcher callback, resMap:$resMap.")

            val grantedPermissions: Array<String> = resMap.entries.filter { it.value }.map { it.key }.toTypedArray()
            if (grantedPermissions.isNotEmpty()) result.onGranted?.invoke(grantedPermissions)


            // Non granted exits.
            if (grantedPermissions.size < this.permissions.size) {

                val nonGrantedPermissions = resMap.entries.filterNot { it.value }.map { it.key }

                val shouldShowRationalePermissions = nonGrantedPermissions
                    .filter { permission -> ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(), permission) }
                    .also { logInner("shouldShowRequestPermissionRationale: $it.") }
                    .toTypedArray()

                val neverAskAgainPermissions = nonGrantedPermissions
                    .filterNot { permission -> ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(), permission) }
                    .also { logInner("never ask again: $it.") }
                    .toTypedArray()

                if (shouldShowRationalePermissions.isNotEmpty()) result.shouldShowRationale?.invoke(shouldShowRationalePermissions)

                if (neverAskAgainPermissions.isNotEmpty()) result.onNeverAskAgain?.invoke(neverAskAgainPermissions)
            }

        //if (isGranted) {
                // Permission is granted. Continue the action or workflow in your
                // app.
            //} else {
                // Explain to the user that the feature is unavailable because the
                // features requires a permission that the user has denied. At the
                // same time, respect the user's decision. Don't link to system
                // settings in an effort to convince the user to change their
                // decision.
           // }
        }

    fun requestPermission(vararg permissions: String, result: PermissionResult) {
        this.permissions = arrayOf(*permissions)
        this.result = result
        if (this.isResumed) requestPermissionLauncher.launch(this.permissions)
    }


    override fun shouldShowRequestPermissionRationale(permission: String): Boolean {
        return super.shouldShowRequestPermissionRationale(permission)
            .also { logInner("shouldShowRequestPermissionRationale, permission:$permission.") }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        logInner("onCreate.")
        // avoid "Operation cannot be started before fragment is in created state"
        requestPermissionLauncher.launch(this.permissions)
    }

    override fun onStart() {
        super.onStart()
        logInner("onStart.")
    }

    override fun onResume() {
        super.onResume()
        logInner("onResume.")
    }

    override fun onStop() {
        super.onStop()
        logInner("onStop.")
    }

    override fun onDestroy() {
        super.onDestroy()
        logInner("onDestroy.")
    }

}
