package me.yangxiaobin.android.kotlin.codelab.base.ability

import android.content.Context
import android.graphics.drawable.Drawable
import androidx.annotation.ColorRes
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import me.yangxiaobin.android.kotlin.codelab.ext.context.getResName

/**
 * val intoString: Int.() -> String = buildResString(context)
 * val text = R.string.abc.intoString()
 */
fun buildResString(context: Context): Int.() -> String =
    fun @receiver:StringRes Int.(): String = context.getString(this)

public fun buildResColor(context: Context): Int.() -> Int =
    fun @receiver:ColorRes Int.(): Int = ContextCompat.getColor(context, this)

fun buildResDrawable(context: Context): Int.() -> Drawable =
    fun @receiver:ColorRes Int.(): Drawable = requireNotNull(
        ContextCompat.getDrawable(
            context,
            this
        )
    ) { "Can NOT found drawable for ${this.getResName(context)}." }


interface ResAbility {

    val int2Color: Int.() -> Int

    val int2String: Int.() -> String

    val int2Drawable: Int.() -> Drawable?

    fun Int.asString() = int2String(this)
    fun Int.asColor() = int2Color(this)
    fun Int.asDrawable(): Drawable? = int2Drawable(this)

}

interface DefaultResAbility : ResAbility {

    val resContext: Context

    override val int2Color: Int.() -> Int
        get() = buildResColor(resContext)

    override val int2String: Int.() -> String
        get() = buildResString(resContext)

    override val int2Drawable: Int.() -> Drawable?
        get() = buildResDrawable(resContext)
}
