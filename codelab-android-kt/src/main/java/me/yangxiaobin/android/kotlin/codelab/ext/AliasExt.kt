package me.yangxiaobin.android.kotlin.codelab.ext

import android.view.ViewGroup



typealias NormalLayoutParams = ViewGroup.LayoutParams
typealias MarginLayoutParams = ViewGroup.MarginLayoutParams

const val WrapContent = ViewGroup.LayoutParams.WRAP_CONTENT
const val MatchParent = ViewGroup.LayoutParams.MATCH_PARENT

public val MatchParentParams get() = NormalLayoutParams(MatchParent, MatchParent)
val WrapContentParams get() = NormalLayoutParams(WrapContent, WrapContent)

val MatchParentMarginParams get() = MarginLayoutParams(MatchParent, MatchParent)
val WrapContentMarginParams get() = MarginLayoutParams(WrapContent, WrapContent)


typealias androidColor = android.graphics.Color
typealias androidView = android.view.View

// This will be warned by IDE
//typealias androidR = android.R


