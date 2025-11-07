package me.yangxiaobin.kotlin.codelab.redux.utils

internal fun isPlainObject(obj: Any): Boolean = obj !is Function<*>
