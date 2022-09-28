package me.yangxiaobin.android.di_lib.pojo

import me.yangxiaobin.android.di_lib.logDI
import javax.inject.Inject

class StuffC @Inject constructor() {

    init {
        logDI("StuffC ${this.hashCode()} init.")
    }

    fun work(){
        logDI("StuffC ${this.hashCode()} work.")
    }
}
