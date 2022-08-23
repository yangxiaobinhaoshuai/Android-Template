package me.yangxiaobin.android.log2disk

import me.yangxiaobin.logger.domain.DomainElement


class LogInterceptor : me.yangxiaobin.logger.uitlity.Interceptor  {


    override val intercept: Boolean get() = TODO("Not yet implemented")


    override fun transform(element: DomainElement?): DomainElement? {
        return element
    }

}
