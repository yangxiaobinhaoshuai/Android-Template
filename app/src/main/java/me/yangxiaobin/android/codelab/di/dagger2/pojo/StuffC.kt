package me.yangxiaobin.android.codelab.di.dagger2.pojo

import me.yangxiaobin.android.codelab.di.dagger2.logDI
import javax.inject.Inject

class StuffC @Inject constructor() {

    init {
        logDI("StuffC ${this.hashCode()} init.")
    }

    fun work(){
        logDI("StuffC ${this.hashCode()} work.")
    }
}
