package com.wkj.common.scripts.logger


// 导入 Gradle 的 Logger API
import org.gradle.api.logging.Logger as GradleApiLogger

/**
 * 一个实现了我们自定义 Logger 接口的类，其内部使用 Gradle 的日志系统。
 *
 * @param gradleLogger 从 Gradle 项目或任务中获取的 Logger 实例。
 * @param defaultTag 当调用者没有提供 tag 时，可以使用的默认 tag。
 */
class GradleLoggerAware(
    private val gradleLogger: GradleApiLogger,
    private val defaultTag: String = "Gradle",
) : LoggerAware { // 实现了我们之前定义的 Logger 接口

    /**
     * 将我们的日志调用映射到 Gradle 的 DEBUG 级别。
     * 只有在运行时使用 -d 或 --debug 参数时才会显示。
     */
    override fun d(tag: String, message: String) {
        // Gradle 的 logger 支持使用 {} 占位符，这比字符串拼接性能更好。
        // 我们在这里手动模拟一下，或者直接拼接。
        if (gradleLogger.isDebugEnabled) {
            gradleLogger.debug("[{}] {}", tag.ifEmpty { defaultTag }, message)
        }
    }

    /**
     * 将我们的日志调用映射到 Gradle 的 LIFECYCLE 级别。
     * 这是默认显示的级别，用于重要的构建信息。
     */
    override fun i(tag: String, message: String) {
        // 使用 lifecycle 级别，因为它在默认情况下是可见的，适合作为 "Info" 级别。
        gradleLogger.lifecycle("[{}] {}", tag.ifEmpty { defaultTag }, message)
    }


    /**
     * 将我们的日志调用映射到 Gradle 的 ERROR 级别。
     */
    override fun e(tag: String, message: String, throwable: Throwable?) {
        // error 方法也可以接受一个 throwable 参数。
        gradleLogger.error("[{}] {}", tag.ifEmpty { defaultTag }, message, throwable)
    }
}
