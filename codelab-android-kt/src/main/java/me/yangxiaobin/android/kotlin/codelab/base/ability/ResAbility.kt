package me.yangxiaobin.android.kotlin.codelab.base.ability

import android.graphics.drawable.Drawable

interface ResAbility {

    val int2Color: Int.() -> Int

    val int2String: Int.() -> String

    val int2Drawable: Int.() -> Drawable

    fun Int.asString() = int2String(this)
    fun Int.asColor() = int2Color(this)
    fun Int.asDrawable() = int2Drawable(this)

}
