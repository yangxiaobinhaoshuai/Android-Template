package me.yangxiaobin.android.codelab

import me.yangxiaobin.android.kotlin.codelab.base.AbsApplication
import me.yangxiaobin.android.kotlin.codelab.ext.getRomName

class MyApp : AbsApplication() {

    override fun onCreate() {
        super.onCreate()
        init()
    }

    private fun init() {

        println("------> Current device rom :${getRomName()}.")
    }

}
