package me.yangxiaobin.android.di_lib.pojo

import me.yangxiaobin.android.di_lib.di_module.MyScope
import me.yangxiaobin.android.di_lib.logDI
import javax.inject.Inject


@MyScope
class StuffB @Inject constructor(){

    init {
        logDI("StuffB ${this.hashCode()} init.")
    }

    fun work() {
        logDI("StuffB ${this.hashCode()} work.")
    }
}
