package me.yangxiaobin.android.permission

import android.os.Bundle
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


    private lateinit var permissionString: String
    private lateinit var result: PermissionResult

    init {
        logInner("PermissionFragment init ,hash : ${this.hashCode()}.")
    }


    // Register the permissions callback, which handles the user's response to the
    // system permissions dialog. Save the return value, an instance of
    // ActivityResultLauncher. You can use either a val, as shown in this snippet,
    // or a lateinit var in your onAttach() or onCreate() method.
    private val requestPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
        logInner("permission request launcher callback, isGranted:$isGranted.")

        result.onResult?.invoke(isGranted)


        if (!isGranted) {
            val should = ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(), permissionString)

            if (should) result.shouldShowRationale?.invoke(permissionString)
            else result.onNeverAskAgain?.invoke(permissionString)
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

    fun requestPermission(permissionString: String, result: PermissionResult) {
        this.permissionString = permissionString
        this.result = result
        if (this.isResumed) requestPermissionLauncher.launch(permissionString)
    }


    override fun shouldShowRequestPermissionRationale(permission: String): Boolean {
        return super.shouldShowRequestPermissionRationale(permission)
            .also { logInner("shouldShowRequestPermissionRationale, permission:$permission.") }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        logInner("onCreate.")
        // avoid "Operation cannot be started before fragment is in created state"
        requestPermissionLauncher.launch(permissionString)
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
