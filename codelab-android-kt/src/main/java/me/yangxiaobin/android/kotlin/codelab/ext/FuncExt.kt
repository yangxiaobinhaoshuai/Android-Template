package me.yangxiaobin.android.kotlin.codelab.ext


public typealias Action = () -> Unit

val emptyAction: Action = {}

typealias TypedAction<T> = (T) -> Unit
