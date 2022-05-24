package me.yangxiaobin.android.kotlin.codelab.ext


fun Boolean?.nullAsFalse(): Boolean = this ?: false

fun Boolean?.nullAsTrue(): Boolean = this ?: true
