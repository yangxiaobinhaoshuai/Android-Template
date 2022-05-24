package me.yangxiaobin.android.permission


/**
 * 有操作结果
 */
typealias OnResult = (Boolean) -> Unit

/**
 * 不再询问
 */
typealias OnNeverAskAgain = (String) -> Unit

/**
 * 展示权限理由
 */
typealias OnShouldShowRationale = (String) -> Unit


