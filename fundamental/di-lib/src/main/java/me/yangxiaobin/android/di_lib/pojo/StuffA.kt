package me.yangxiaobin.android.di_lib.pojo

import me.yangxiaobin.android.di_lib.logDI
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class StuffA @Inject constructor() {

    init {
        logDI("StuffA ${this.hashCode()} init.")
    }

    fun work() {
        logDI("StuffA ${this.hashCode()} work.")
    }
}
