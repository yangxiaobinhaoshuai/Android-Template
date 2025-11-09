package com.wkj.common.scripts.logger


interface LoggerAware {
    fun i(tag: String, message: String)
    fun d(tag: String, message: String)
    fun e(tag: String, message: String, throwable: Throwable? = null)
}

interface TaggedLogger {
    fun i(message: String)
    fun d(message: String)
    fun e(message: String, throwable: Throwable? = null)
}

private class TaggedLoggerImpl(
    private val logger: LoggerAware,
    private val tag: String,
) : TaggedLogger {

    override fun i(message: String) {
        logger.i(tag, message)
    }

    override fun d(message: String) {
        logger.d(tag, message)
    }

    override fun e(message: String, throwable: Throwable?) {
        logger.e(tag, message, throwable)
    }
}


fun LoggerAware.withTag(tag: String): TaggedLogger {
    return TaggedLoggerImpl(this, tag)
}


/**
 * 全局日志访问入口。
 * 这是一个单例对象，通过委托模式将调用转发给具体的 Logger 实现。
 */
object Log : LoggerAware {
    // 默认是一个不做任何事情的空实现，防止在初始化前调用时崩溃
    private var loggerAware: LoggerAware = NoOpLoggerAware()

    /**
     * 初始化 Logger，必须在应用程序启动时调用。
     * @param newLoggerAware 具体的 Logger 实现 (JvmLogger 或 AndroidLogger)
     */
    fun init(newLoggerAware: LoggerAware) {
        loggerAware = newLoggerAware
    }

    override fun d(tag: String, message: String) {
        loggerAware.d(tag, message)
    }

    override fun i(tag: String, message: String) {
        loggerAware.i(tag, message)
    }

    override fun e(tag: String, message: String, throwable: Throwable?) {
        loggerAware.e(tag, message, throwable)
    }

    /**
     * 一个空操作的 Logger 实现，用作默认值。
     */
    private class NoOpLoggerAware : LoggerAware {
        override fun d(tag: String, message: String) {}
        override fun i(tag: String, message: String) {}
        override fun e(tag: String, message: String, throwable: Throwable?) {}
    }
}

