package me.yangxiaobin.android.permission

import kotlinx.coroutines.CancellableContinuation
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume


sealed interface PermissionResponse {

    object Success : PermissionResponse {
        override fun toString(): String = "PermissionResponse.Success"
    }

    sealed interface Denied : PermissionResponse {

        /**
         * 首次被拒绝
         */
        object ShowRationale : Denied {
            override fun toString(): String = "PermissionResponse.Denied.ShowRationale"
        }

        object NeverAskAgain : Denied {
            override fun toString(): String = "PermissionResponse.Denied.NeverAskAgain"
        }
    }

}

suspend fun PermissionRequest.requestAsync(permissionString: String): PermissionResponse =
    suspendCancellableCoroutine { coun: CancellableContinuation<PermissionResponse> ->
        this.request(permissionString) {
            onGranted {
                coun.resume(PermissionResponse.Success)
            }
            shouldShowRationale {
                coun.resume(PermissionResponse.Denied.ShowRationale)
            }
            onNeverAskAgain {
                coun.resume(PermissionResponse.Denied.NeverAskAgain)
            }
        }
    }

