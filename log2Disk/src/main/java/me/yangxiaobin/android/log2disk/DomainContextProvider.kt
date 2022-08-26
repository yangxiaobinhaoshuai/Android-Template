package me.yangxiaobin.android.log2disk

import me.yangxiaobin.android.mids.StaticAddFuntion
import me.yangxiaobin.logger.core.LogLevel
import me.yangxiaobin.logger.domain.DomainContext
import me.yangxiaobin.logger.domain.DomainElement
import me.yangxiaobin.logger.elements.InterceptorLogElement
import me.yangxiaobin.logger.elements.LogPrinterDelegate
import me.yangxiaobin.logger.elements.LogPrinterLogElement
import me.yangxiaobin.logger.uitlity.DomainElementInterceptor
import me.yangxiaobin.logger.uitlity.LogPrinter


class PrinterProxy(private val origin: LogPrinter) : LogPrinterDelegate(origin) {

    // TODO
    override fun print(level: LogLevel, tag: String, message: String) {
        origin.print(level, tag, "$message I'm PrinterProxy")
    }

}

class LogInterceptor : DomainElementInterceptor {

    override fun transform(element: DomainElement?): DomainElement? =
        if (element !is LogPrinterLogElement) element else LogPrinterLogElement(PrinterProxy(element.logPrinter))

    override fun wantIntercept(element: DomainElement?): Boolean = element is LogPrinterLogElement
}

object DomainContextProvider {

    @StaticAddFuntion
    fun provide(): DomainContext = InterceptorLogElement(LogInterceptor())

}
