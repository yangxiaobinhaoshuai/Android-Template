package me.yangxiaobin.android.di_lib

import me.yangxiaobin.logger.RawLogger


val logDI = fun(message: String) = RawLogger.clone().d("DI-Lib", message)
