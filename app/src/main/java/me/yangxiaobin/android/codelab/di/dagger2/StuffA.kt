package me.yangxiaobin.android.codelab.di.dagger2

import javax.inject.Inject

class StuffA @Inject constructor() {

    init {
        logDI("StuffA init.")
    }

    fun work(){
        logDI("StuffA work.")
    }
}
