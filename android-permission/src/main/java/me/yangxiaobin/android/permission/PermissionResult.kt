package me.yangxiaobin.android.permission

typealias ResultBuilder = PermissionResultBuilder.() -> Unit


data class PermissionResult(
    val onResult: OnResult? = null,
    val onNeverAskAgain: OnNeverAskAgain? = null,
    val shouldShowRationale: OnShouldShowRationale? = null,
)


class PermissionResultBuilder {

    var permissionResult = PermissionResult()
        private set

    fun onResult(result: OnResult) {
        permissionResult = permissionResult.copy(onResult = result)
    }

    fun onNeverAskAgain(onNeverAskAgain: OnNeverAskAgain) {
        permissionResult = permissionResult.copy(onNeverAskAgain = onNeverAskAgain)
    }

    fun shouldShowRationale(shouldShowRationale: OnShouldShowRationale) {
        permissionResult = permissionResult.copy(shouldShowRationale = shouldShowRationale)
    }

}

