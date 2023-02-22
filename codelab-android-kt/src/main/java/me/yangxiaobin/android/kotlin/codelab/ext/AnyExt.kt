package me.yangxiaobin.android.kotlin.codelab.ext


public inline fun <reified T : Any?> T.convertIf(meetCondition: Boolean, conversion: (T) -> T): T = when {
    this == null -> null as T
    meetCondition -> conversion.invoke(this)
    else -> this
}

public inline fun <reified T : Any?> T.convertIf(meetCondition: T.() -> Boolean, conversion: (T) -> T): T = when {
    this == null -> null as T
    meetCondition.invoke(this) -> conversion.invoke(this)
    else -> this
}
