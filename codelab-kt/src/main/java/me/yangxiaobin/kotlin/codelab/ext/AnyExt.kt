package me.yangxiaobin.kotlin.codelab.ext


val Any?.neatName: String get() = this?.javaClass?.simpleName ?: "NULL"
