package me.yangxiaobin.android.kotlin.codelab.ext

import android.view.View


val View.getScreenLocation: Pair<Int, Int>
    get() {
        val posPair = IntArray(2)
        this.getLocationOnScreen(posPair)
        return posPair[0] to posPair[1]
    }


/**
 * 获取 View 的 res Name, 如 : R.string.user_name -> user_name
 */
val View.getResName: String?
    get() {
        if (this.id == View.NO_ID) return null
        return runCatching { this.context.resources.getResourceEntryName(this.id) }.getOrNull()
    }
