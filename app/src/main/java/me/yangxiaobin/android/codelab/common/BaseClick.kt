package me.yangxiaobin.android.codelab.common

import me.yangxiaobin.android.kotlin.codelab.log.L

fun interface BaseClick<T> : Function1<T, Unit> {

    override fun invoke(p1: T) {
        L.i("BaseClick", "param :$p1")
        click(p1)
    }

    fun click(p1: T)
}

