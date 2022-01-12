package me.yangxiaobin.android.codelab.di.dagger2

import javax.inject.Inject

class StuffB @Inject constructor() {

    init {
        logDI("StuffB init.")
    }

    fun work(){
        logDI("StuffB work.")
    }
}
