package me.yangxiaobin.android.codelab.di.dagger2.pojo

import me.yangxiaobin.android.codelab.di.dagger2.logDI
import javax.inject.Inject

class StuffA @Inject constructor() {

    init {
        logDI("StuffA ${this.hashCode()} init.")
    }

    fun work(){
        logDI("StuffA ${this.hashCode()} work.")
    }
}
