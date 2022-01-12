package me.yangxiaobin.android.codelab.di.dagger2

import javax.inject.Inject


class StuffManager @Inject constructor() {

    init {
        logDI("StuffManager init.")
    }

    fun work(){
        logDI("We work hard!!")
    }
}
