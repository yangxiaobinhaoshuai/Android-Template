package me.yangxiaobin.kotlin.codelab.ext


fun getFullStacktrace() = getLimitStacktrace(Int.MAX_VALUE)

/**
 * Example :
 *
 *     me.yangxiaobin.kotlin.codelab.ext.StacktraceExtKt.getLimitStacktrace(StacktraceExt.kt:7)
 *     me.yangxiaobin.kotlin.codelab.ext.StacktraceExtKt.getLimitStacktrace$default(StacktraceExt.kt:6)
 *
 *     me.yangxiaobin.android.codelab.MainActivity.checkPermission(MainActivity.kt:168)
 *     androidx.core.content.ContextCompat.checkSelfPermission(ContextCompat.java:598)
 *     me.yangxiaobin.android.permission.PermissionManager.checkGranted(PermissionManager.kt:103).
 *
 *
 *     @see android.util.Log.getStackTraceString(Throwable()))
 */
fun getLimitStacktrace(depth: Int = 5) =
    Throwable().stackTrace
        .drop(2)
        .take(depth)
        .joinToString(separator = "\n\t")

fun getCurMethodName(): String {
    val stacktrace: String = Throwable().stackTrace.first().toString()

    val dot = '.'
    val count = stacktrace.toCharArray().count(dot::equals)
    return stacktrace.split(dot, limit = count - 1).last()
}
