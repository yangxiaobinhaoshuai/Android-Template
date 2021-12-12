package me.yangxiaobin.android.kotlin.codelab.ext

import android.os.Bundle


fun Bundle?.dumpContentToString(): String {

    val content = this?.keySet()
        ?.joinToString(separator = ",") { key: String -> "$key : ${this.get(key).dumpToString()}" }
        ?: ""

    return "Bundle[$content]"
}

// TODO 待完善
fun Any?.dumpToString(): String = when (this) {
    is Array<*> -> this.contentToString()
    is List<*> -> this.toString()
    is Map<*, *> -> this.toString()
    else -> ""
}
