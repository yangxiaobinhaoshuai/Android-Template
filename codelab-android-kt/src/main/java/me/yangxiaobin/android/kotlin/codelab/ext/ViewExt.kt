package me.yangxiaobin.android.kotlin.codelab.ext

import android.view.View


val View.getScreenLocation: Pair<Int, Int>
    get() {
        val posPair = IntArray(2)
        this.getLocationOnScreen(posPair)
        return posPair[0] to posPair[1]
    }
