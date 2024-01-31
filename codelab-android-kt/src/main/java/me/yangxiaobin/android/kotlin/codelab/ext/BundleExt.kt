package me.yangxiaobin.android.kotlin.codelab.ext

import android.os.BaseBundle


fun BaseBundle?.dumpContentToString(): String {

    val content: String = this?.keySet()
        ?.joinToString(separator = ",") { key: String -> "($key : ${this.get(key)})" } ?: ""

    return "Bundle[$content]"
}

// TODO 待完善
fun Any?.dumpToString(): String = when (this) {
    is Array<*> -> this.contentToString()
    is List<*> -> this.toString()
    is Map<*, *> -> this.toString()
    is BaseBundle -> this.dumpContentToString()
    else -> "Type for (${this?.javaClass?.simpleName}) to be considered."
}
