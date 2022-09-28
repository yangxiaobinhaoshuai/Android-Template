package me.yangxiaobin.android.permission

typealias ResultBuilder = PermissionResultBuilder.() -> Unit


data class PermissionResult(
    val onGranted: OnGranted? = null,
    val onNeverAskAgain: OnNeverAskAgain? = null,
    val shouldShowRationale: OnShouldShowRationale? = null,
){
    companion object{
        val EMPTY_RESULT = PermissionResult()
    }
}


class PermissionResultBuilder {

    var permissionResult = PermissionResult()
        private set

    fun onGranted(result: OnGranted) {
        permissionResult = permissionResult.copy(onGranted = result)
    }

    /**
     * 第二次及以后被拒绝
     */
    fun onNeverAskAgain(onNeverAskAgain: OnNeverAskAgain) {
        permissionResult = permissionResult.copy(onNeverAskAgain = onNeverAskAgain)
    }

    /**
     * 第一次被拒绝
     */
    fun shouldShowRationale(shouldShowRationale: OnShouldShowRationale) {
        permissionResult = permissionResult.copy(shouldShowRationale = shouldShowRationale)
    }

}

