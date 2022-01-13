package me.yangxiaobin.android.codelab.di.dagger2.pojo

import me.yangxiaobin.android.codelab.di.dagger2.di_module.MyScope
import me.yangxiaobin.android.codelab.di.dagger2.logDI
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
