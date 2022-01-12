package me.yangxiaobin.android.codelab.di.dagger2

import me.yangxiaobin.android.codelab.log.appLogger
import me.yangxiaobin.logger.clone


val logDI = fun(message: String) = appLogger.clone().d("DI ==>", message)
