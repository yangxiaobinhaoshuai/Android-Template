package me.yangxiaobin.android.kotlin.codelab.ext

import android.os.Handler
import android.os.Looper
import android.os.Message


val mainLopper get() = Looper.getMainLooper()

val mainHandler get() = Handler(mainLopper)

fun getMainHandler(handleMessage: (Message) -> Unit): Handler = object : Handler(mainLopper) {
    override fun handleMessage(msg: Message) {
        super.handleMessage(msg)
        handleMessage(msg)
    }
}
