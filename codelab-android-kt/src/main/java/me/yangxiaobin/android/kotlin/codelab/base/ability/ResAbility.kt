package me.yangxiaobin.android.kotlin.codelab.base.ability

import android.graphics.drawable.Drawable

interface ResAbility {

    val asColor: Int.() -> Int

    val asString: Int.() -> String

    val asDrawable: Int.() -> Drawable

}