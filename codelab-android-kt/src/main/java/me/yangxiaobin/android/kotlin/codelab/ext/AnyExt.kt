package me.yangxiaobin.android.kotlin.codelab.ext


val Any?.simpleName: String get() = if (this != null) this::class.java.simpleName else "NULL"

inline fun <reified T : Any?> T.convertIf(meetCondition: Boolean, conversion: (T) -> T): T = when {
    this == null -> null as T
    meetCondition -> conversion.invoke(this)
    else -> this
}
