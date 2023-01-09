package com.yxb.kv

import android.content.SharedPreferences


/**
 * Jetpack DataStore : https://juejin.cn/post/7109395564789235720
 *
 * SP 原理分析 : https://juejin.cn/post/7169265620306165790
 */


// TODO
interface KvDispatcher : SharedPreferences, SharedPreferences.Editor {

    fun set() {

    }

    fun get() {

    }

}

