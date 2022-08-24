package me.yangxiaobin.android.kotlin.codelab.log

import android.util.Log
import me.yangxiaobin.logger.RawLogger
import me.yangxiaobin.logger.core.LogFacade
import me.yangxiaobin.logger.core.LogLevel
import me.yangxiaobin.logger.domain.DomainElement
import me.yangxiaobin.logger.elements.InterceptorLogElement
import me.yangxiaobin.logger.elements.LogPrinterDelegate
import me.yangxiaobin.logger.elements.LogPrinterLogElement
import me.yangxiaobin.logger.uitlity.DomainElementInterceptor
import me.yangxiaobin.logger.uitlity.LogPrinter

object L : LogFacade by RawLogger

class PrinterProxy(private val origin: LogPrinter) : LogPrinterDelegate(origin) {

    override fun print(level: LogLevel, tag: String, message: String) {
        origin.print(level, tag, message)

    }

}

class LogInterceptor : DomainElementInterceptor {

    override fun transform(element: DomainElement?): DomainElement? =
        if (element !is LogPrinterLogElement) element else LogPrinterLogElement(PrinterProxy(element.logPrinter))

    override fun wantIntercept(element: DomainElement?): Boolean = element is LogPrinterLogElement
}

val AndroidLogger = L.clone(
    newLogContext = LogPrinterLogElement(AndroidPrinter()) + InterceptorLogElement(LogInterceptor())
)

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
