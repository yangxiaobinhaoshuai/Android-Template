package me.yangxiaobin.kotlin.codelab.ext


fun getFullStacktrace() = getLimitStacktrace(Int.MAX_VALUE)

fun getLimitStacktrace(depth: Int) = "current stacktrace:\n\t" +
        Throwable().stackTrace
            .drop(1)
            .take(depth)
            .joinToString(separator = "\n\t")
