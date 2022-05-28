package me.yangxiaobin.android_kcp

import me.yangxiaobin.logger.RawLogger
import me.yangxiaobin.logger.clone

/**
 * Plugin log
 */
internal object PLog {

    private const val LOG_TAG = "kcp_plugin"

    private val innerLogger = RawLogger.clone(globalTagPrefix = " -> ")


    fun i(message: String) {
        innerLogger.i(LOG_TAG, message)
    }

    fun d(message: String) {
        innerLogger.d(LOG_TAG, message)
    }

    fun e(message: String) {
        innerLogger.e(LOG_TAG, message)
    }
}
