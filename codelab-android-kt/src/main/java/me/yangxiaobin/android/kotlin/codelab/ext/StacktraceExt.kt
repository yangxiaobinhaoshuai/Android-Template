package me.yangxiaobin.android.kotlin.codelab.ext

import android.os.Build
import me.yangxiaobin.kotlin.codelab.ext.getCurMethodName
import java.util.regex.Pattern


private const val MAX_TAG_LENGTH = 23

@Suppress("PrivatePropertyName")
private val ANONYMOUS_CLASS = Pattern.compile("(\\$\\d+)+$")

fun getCurMethodNameAsTag(): String {
    val tag = getCurMethodName()
    // Tag length limit was removed in API 26.
    return if (tag.length <= MAX_TAG_LENGTH || Build.VERSION.SDK_INT >= 26) {
        tag
    } else {
        tag.substring(0, MAX_TAG_LENGTH)
    }
}
