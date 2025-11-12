package com.wkj.common.scripts.annotation


/**
 * Usage for library code, not for plugin.
 */

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class TimeTrack(
    val tag: String = "TimeTrack",
    val unit: TimeUnit = TimeUnit.MILLISECONDS
)

enum class TimeUnit {
    NANOSECONDS,
    MICROSECONDS,
    MILLISECONDS,
    SECONDS
}
