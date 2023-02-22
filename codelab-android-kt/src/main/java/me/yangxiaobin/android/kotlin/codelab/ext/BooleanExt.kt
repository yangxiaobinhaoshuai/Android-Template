package me.yangxiaobin.android.kotlin.codelab.ext


public fun Boolean?.nullAsFalse(): Boolean = this ?: false

public fun Boolean?.nullAsTrue(): Boolean = this ?: true
