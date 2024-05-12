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
fun getLimitStacktrace(
    depth: Int = 5,
    drop: Int = 1,
    separator: CharSequence = "\n\t",
): String = Throwable().stackTrace
    .drop(drop)
    .take(depth)
    .joinToString(separator = separator)

fun getCurMethodName(): String {
    val stacktrace: String = Throwable().stackTrace.first().toString()

    val dot = '.'
    val count = stacktrace.toCharArray().count(dot::equals)
    return stacktrace.split(dot, limit = count - 1).last()
}
