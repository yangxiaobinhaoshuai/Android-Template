package me.yangxiaobin.android.kotlin.codelab.ext


typealias Action = () -> Unit

val emptyAction: Action = {}

typealias TypedAction<T> = (T) -> Unit
