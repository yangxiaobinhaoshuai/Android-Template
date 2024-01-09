package me.yangxiaobin.android.kotlin.codelab.log

import android.util.Log
import me.yangxiaobin.logger.RawLogger
import me.yangxiaobin.logger.core.LogFacade
import me.yangxiaobin.logger.core.LogLevel
import me.yangxiaobin.logger.domain.DomainContext
import me.yangxiaobin.logger.domain.EmptyDomainContext
import me.yangxiaobin.logger.elements.LogPrinterLogElement
import me.yangxiaobin.logger.uitlity.LogPrinter

object L : LogFacade by RawLogger

public val AndroidLogger = L.clone(
    newLogContext = LogPrinterLogElement(AndroidPrinter()) + LogElementProvider.elements.fold(EmptyDomainContext, DomainContext::plus)
)

/**
 * IOC example
 */
object LogElementProvider {

    val elements: MutableList<DomainContext> = mutableListOf()
}

class AndroidPrinter : LogPrinter {
    override fun print(level: LogLevel, tag: String, message: String) {
        val intLevel = when (level) {
            LogLevel.VERBOSE -> Log.VERBOSE
            LogLevel.DEBUG -> Log.DEBUG
            LogLevel.INFO -> Log.INFO
            LogLevel.ERROR -> Log.ERROR
        }
        Log.println(intLevel, tag, message)
    }
}
