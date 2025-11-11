package com.wkj.common.scripts.annotation


// Provide for source code
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
