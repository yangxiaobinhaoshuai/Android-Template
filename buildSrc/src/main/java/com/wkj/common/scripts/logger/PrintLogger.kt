package com.wkj.common.scripts.logger


import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

/**
 * 用于纯 JVM 环境的 Logger 实现。
 * 使用 println 将日志输出到标准控制台。
 */
class JvmLoggerAware : LoggerAware {
    private val formatter = DateTimeFormatter.ofPattern("HH:mm:ss.SSS")

    private fun printWithInfo(level: String, tag: String, message: String) {
        val time = LocalDateTime.now().format(formatter)
        println("$time $level/$tag: $message")
    }

    override fun d(tag: String, message: String) {
        printWithInfo("D", tag, message)
    }

    override fun i(tag: String, message: String) {
        printWithInfo("I", tag, message)
    }


    override fun e(tag: String, message: String, throwable: Throwable?) {
        printWithInfo("E", tag, message)
        throwable?.printStackTrace()
    }
}
