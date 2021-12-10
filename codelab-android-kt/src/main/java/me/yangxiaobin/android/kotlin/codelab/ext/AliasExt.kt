package me.yangxiaobin.android.kotlin.codelab.ext

import android.view.ViewGroup



typealias NormalLayoutParams = ViewGroup.LayoutParams

const val WrapContent = ViewGroup.LayoutParams.WRAP_CONTENT
const val MatchParent = ViewGroup.LayoutParams.MATCH_PARENT

val MatchParentParams get() = NormalLayoutParams(MatchParent, MatchParent)
val WrapContentParams get() = NormalLayoutParams(WrapContent, WrapContent)
