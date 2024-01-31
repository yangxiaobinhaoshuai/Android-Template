package me.yangxiaobin.android.permission


typealias AndroidPermission = android.Manifest.permission

/**
 * 有操作结果
 */
typealias OnGranted = (Array<String>) -> Unit

/**
 * 不再询问
 */
typealias OnNeverAskAgain = (Array<String>) -> Unit

/**
 * 展示权限理由
 */
typealias OnShouldShowRationale = (Array<String>) -> Unit


