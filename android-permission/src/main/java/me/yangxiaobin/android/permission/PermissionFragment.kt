package me.yangxiaobin.android.permission

import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import kotlin.properties.Delegates
import kotlin.reflect.KProperty

/**
 *  请求应用权限: https://developer.android.com/training/permissions/requesting
 */
class PermissionFragment : Fragment() {


    private lateinit var diode:PermissionRequest.Diode
    private lateinit var permissionString: String

    init {
        logInner("PermissionFragment init ,hash : ${this.hashCode()}.")
    }


    // Register the permissions callback, which handles the user's response to the
    // system permissions dialog. Save the return value, an instance of
    // ActivityResultLauncher. You can use either a val, as shown in this snippet,
    // or a lateinit var in your onAttach() or onCreate() method.
    private val requestPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
        logInner("permission request launcher callback, isGranted:$isGranted.")

        diode.isGranted?.invoke(isGranted)

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

    fun requestPermission(permissionString: String, diode: PermissionRequest.Diode) {
        logInner("requestPermission :#$permissionString, diode : $diode.")
        this.diode = diode
        this.permissionString = permissionString
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
