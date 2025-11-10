package com.wkj.common.scripts.logger

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.api.logging.Logging

internal inline val <reified T> T.myLogger: TaggedLogger where T : Project
    get() {
        return taggedLogger()
    }

internal inline val <reified T> T.myLogger: TaggedLogger where T : Task
    get() {
        return taggedLogger()
    }


internal inline val <reified T> T.myLogger: TaggedLogger where T : Plugin<Project>
    get() {
        return taggedLogger()
    }

internal inline fun <reified T> T.taggedLogger(): TaggedLogger {
    if (this == null) return NoOpLoggerAware.withTag("NoOpLogger")
    val simpleName: String = requireNotNull(T::class.simpleName)
    { "Cant retrieve logger from null obj: ${getLimitStacktrace(2)}" }
    val gradleLogger = Logging.getLogger(simpleName)
    val loggerAware = GradleLoggerAware(gradleLogger, simpleName)
    return loggerAware.withTag(simpleName)
}


fun getLimitStacktrace(
    depth: Int = 5,
    drop: Int = 1,
    separator: CharSequence = "\n\t",
): String = Throwable().stackTrace
    .drop(drop)
    .take(depth)
    .joinToString(separator = separator)
